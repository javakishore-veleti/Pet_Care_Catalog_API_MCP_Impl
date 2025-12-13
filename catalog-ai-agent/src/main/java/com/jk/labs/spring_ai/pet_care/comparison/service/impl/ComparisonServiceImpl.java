package com.jk.labs.spring_ai.pet_care.comparison.service.impl;

import com.jk.labs.spring_ai.pet_care.common.mcp.model.ToolResponse;
import com.jk.labs.spring_ai.pet_care.common.mcp.service.McpClientService;
import com.jk.labs.spring_ai.pet_care.comparison.config.ComparisonConfig;
import com.jk.labs.spring_ai.pet_care.comparison.model.ComparisonRequest;
import com.jk.labs.spring_ai.pet_care.comparison.model.ComparisonResponse;
import com.jk.labs.spring_ai.pet_care.comparison.model.ComparisonResponse.*;
import com.jk.labs.spring_ai.pet_care.comparison.service.ComparisonService;
import com.jk.labs.spring_ai.pet_care.research.model.ResearchRequest;
import com.jk.labs.spring_ai.pet_care.research.model.ResearchResponse;
import com.jk.labs.spring_ai.pet_care.research.service.ResearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComparisonServiceImpl implements ComparisonService {

    private static final String AGENT_NAME = "Comparison Agent";

    private final McpClientService mcpClientService;
    private final ResearchService researchService;
    private final ComparisonConfig config;

    // Cache for packages fetched via searchPackages
    private Map<String, Map<String, Object>> packageCache = new HashMap<>();

    @Override
    public ComparisonResponse compare(ComparisonRequest request) {
        log.info("{} - Comparing packages: {}", AGENT_NAME, request.getPackageCodes());

        // Validate request
        validateRequest(request);

        // Step 1: Fetch ALL packages first to populate cache
        fetchAllPackages();

        // Step 2: Get package details for requested packages
        List<PackageComparison> packages = fetchPackageDetails(request.getPackageCodes());

        // Step 3: Build feature matrix
        FeatureMatrix featureMatrix = buildFeatureMatrix(packages, request.getFocusFeatures());

        // Step 4: Build price comparison
        PriceComparison priceComparison = buildPriceComparison(packages);

        // Step 5: Determine winner based on priorities
        ComparisonWinner winner = determineWinner(packages, request.getPriorities(),
                request.getMaxBudget(), request.getPetType());

        // Step 6: Generate summary and key differences
        String summary = generateSummary(packages, winner, request);
        List<String> keyDifferences = identifyKeyDifferences(packages, featureMatrix);

        // Calculate confidence
        double confidence = calculateConfidence(packages);

        return ComparisonResponse.builder()
                .agentName(AGENT_NAME)
                .packages(packages)
                .featureMatrix(featureMatrix)
                .priceComparison(priceComparison)
                .winner(winner)
                .summary(summary)
                .keyDifferences(keyDifferences)
                .confidence(confidence)
                .nextAgentSuggestion(winner != null ? "Sales Agent" : null)
                .build();
    }

    @Override
    public ComparisonResponse quickCompare(String packageCode1, String packageCode2) {
        ComparisonRequest request = ComparisonRequest.builder()
                .packageCodes(List.of(packageCode1, packageCode2))
                .build();
        return compare(request);
    }

    @Override
    public ComparisonResponse compareMultiple(List<String> packageCodes) {
        ComparisonRequest request = ComparisonRequest.builder()
                .packageCodes(packageCodes)
                .build();
        return compare(request);
    }

    @Override
    public ComparisonResponse compareWithFocus(List<String> packageCodes, List<String> focusFeatures) {
        ComparisonRequest request = ComparisonRequest.builder()
                .packageCodes(packageCodes)
                .focusFeatures(focusFeatures)
                .build();
        return compare(request);
    }

    @Override
    public ComparisonResponse compareForPet(List<String> packageCodes, String petType, Integer ageYears) {
        ComparisonRequest request = ComparisonRequest.builder()
                .packageCodes(packageCodes)
                .petType(petType)
                .petAgeYears(ageYears)
                .build();
        return compare(request);
    }

    // ==================== Private Helper Methods ====================

    private void validateRequest(ComparisonRequest request) {
        if (request.getPackageCodes() == null || request.getPackageCodes().size() < 2) {
            throw new IllegalArgumentException("At least 2 packages are required for comparison");
        }
        if (request.getPackageCodes().size() > config.getMaxPackagesToCompare()) {
            throw new IllegalArgumentException("Cannot compare more than " +
                    config.getMaxPackagesToCompare() + " packages at once");
        }
    }

    /**
     * Fetch all packages using searchPackages and cache them
     */
    @SuppressWarnings("unchecked")
    private void fetchAllPackages() {
        packageCache.clear();

        // Search for all packages (empty criteria = all)
        ToolResponse response = mcpClientService.searchPackages(new HashMap<>());

        if (response.getSuccess() && response.getContent() != null) {
            try {
                Map<String, Object> content = (Map<String, Object>) response.getContent();
                Object packagesObj = content.get("packages");

                if (packagesObj instanceof List) {
                    List<Map<String, Object>> packages = (List<Map<String, Object>>) packagesObj;
                    for (Map<String, Object> pkg : packages) {
                        String code = (String) pkg.get("code");
                        if (code != null) {
                            packageCache.put(code, pkg);
                            log.debug("Cached package: {}", code);
                        }
                    }
                }
                log.info("Cached {} packages from MCP", packageCache.size());
            } catch (Exception e) {
                log.warn("Error parsing packages from search: {}", e.getMessage());
            }
        }

        // Also try to fetch DOG and CAT specific packages
        for (String petType : List.of("DOG", "CAT")) {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("petType", petType);
            ToolResponse petResponse = mcpClientService.searchPackages(criteria);

            if (petResponse.getSuccess() && petResponse.getContent() != null) {
                try {
                    Map<String, Object> content = (Map<String, Object>) petResponse.getContent();
                    Object packagesObj = content.get("packages");

                    if (packagesObj instanceof List) {
                        List<Map<String, Object>> packages = (List<Map<String, Object>>) packagesObj;
                        for (Map<String, Object> pkg : packages) {
                            String code = (String) pkg.get("code");
                            if (code != null && !packageCache.containsKey(code)) {
                                packageCache.put(code, pkg);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.warn("Error parsing {} packages: {}", petType, e.getMessage());
                }
            }
        }
    }

    private List<PackageComparison> fetchPackageDetails(List<String> packageCodes) {
        List<PackageComparison> packages = new ArrayList<>();

        for (String code : packageCodes) {
            PackageComparison pkg = null;

            // First try cache (populated by fetchAllPackages)
            if (packageCache.containsKey(code)) {
                pkg = parsePackageFromMap(packageCache.get(code), code);
            }

            // If not in cache, try getPackageDetails
            if (pkg == null || pkg.getPackageName() == null) {
                ToolResponse response = mcpClientService.getPackageDetails(code);
                if (response.getSuccess() && response.getContent() != null) {
                    pkg = parsePackageResponse(response.getContent(), code);
                }
            }

            // If still null, try research
            if (pkg == null || pkg.getPackageName() == null) {
                pkg = fetchFromResearch(code);
            }

            // Final fallback - create smart placeholder based on code
            if (pkg == null || pkg.getPackageName() == null) {
                log.warn("Using fallback for package: {}", code);
                pkg = createSmartPlaceholder(code);
            }

            packages.add(pkg);
        }

        return packages;
    }

    @SuppressWarnings("unchecked")
    private PackageComparison parsePackageFromMap(Map<String, Object> data, String requestedCode) {
        try {
            String code = (String) data.getOrDefault("code", requestedCode);
            String name = (String) data.get("name");
            String description = (String) data.get("description");

            // Try multiple price field names
            BigDecimal basePrice = toBigDecimal(data.get("basePrice"));
            BigDecimal monthlyPrice = toBigDecimal(data.get("monthlyPrice"));
            BigDecimal annualPrice = toBigDecimal(data.get("annualPrice"));

            // Calculate prices if not directly available
            if (monthlyPrice == null && basePrice != null) {
                // Assume basePrice is annual, convert to monthly
                monthlyPrice = basePrice.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            }
            if (annualPrice == null && monthlyPrice != null) {
                annualPrice = monthlyPrice.multiply(BigDecimal.valueOf(12));
            }
            if (annualPrice == null && basePrice != null) {
                annualPrice = basePrice;
            }

            // Get boolean flags
            Boolean includesDental = toBoolean(data.get("includesDental"));
            Boolean includesSpayNeuter = toBoolean(data.get("includesSpayNeuter"));

            // Get age group and care level
            String ageGroup = toString(data.get("ageGroup"));
            String careLevel = toString(data.get("careLevel"));

            // Build services list
            List<String> services = extractServicesFromMap(data);

            // Calculate value score
            Double valueScore = calculateValueScore(monthlyPrice, services.size(),
                    includesDental, includesSpayNeuter);

            return PackageComparison.builder()
                    .packageCode(code)
                    .packageName(name)
                    .description(description)
                    .monthlyPrice(monthlyPrice)
                    .annualPrice(annualPrice)
                    .includedServices(services)
                    .serviceCount(services.size())
                    .includesDental(includesDental)
                    .includesSpayNeuter(includesSpayNeuter)
                    .targetAgeGroup(ageGroup)
                    .careLevel(careLevel)
                    .valueScore(valueScore)
                    .build();

        } catch (Exception e) {
            log.error("Error parsing package map for {}: {}", requestedCode, e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private PackageComparison parsePackageResponse(Object content, String requestedCode) {
        try {
            Map<String, Object> data;

            if (content instanceof Map) {
                data = (Map<String, Object>) content;
                // Check if nested under "package" key
                if (data.containsKey("package")) {
                    data = (Map<String, Object>) data.get("package");
                }
            } else {
                return null;
            }

            return parsePackageFromMap(data, requestedCode);

        } catch (Exception e) {
            log.error("Error parsing MCP response for {}: {}", requestedCode, e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> extractServicesFromMap(Map<String, Object> data) {
        List<String> services = new ArrayList<>();

        // Try different field names
        Object servicesObj = data.get("services");
        if (servicesObj == null) servicesObj = data.get("includedServices");

        if (servicesObj instanceof List) {
            for (Object s : (List<?>) servicesObj) {
                if (s instanceof String) {
                    services.add((String) s);
                } else if (s instanceof Map) {
                    // Service might be an object with name field
                    Map<String, Object> serviceMap = (Map<String, Object>) s;
                    String serviceName = (String) serviceMap.get("name");
                    if (serviceName != null) {
                        services.add(serviceName);
                    }
                }
            }
        }

        // Add inferred services based on flags
        if (services.isEmpty()) {
            services.add("Wellness Exams");
            services.add("Basic Vaccinations");

            if (toBoolean(data.get("includesDental"))) {
                services.add("Dental Cleaning");
            }
            if (toBoolean(data.get("includesSpayNeuter"))) {
                services.add("Spay/Neuter");
            }
        }

        return services;
    }

    private PackageComparison fetchFromResearch(String packageCode) {
        ResearchRequest researchRequest = ResearchRequest.builder()
                .query("package " + packageCode)
                .depth(1)
                .build();

        ResearchResponse response = researchService.research(researchRequest);

        if (response.getFindings() != null && !response.getFindings().isEmpty()) {
            for (Map<String, Object> finding : response.getFindings()) {
                if ("packages".equals(finding.get("type"))) {
                    Object data = finding.get("data");
                    if (data instanceof Map) {
                        return parsePackageFromMap((Map<String, Object>) data, packageCode);
                    }
                }
            }
        }

        return null;
    }

    /**
     * Create a smart placeholder by parsing the package code
     */
    private PackageComparison createSmartPlaceholder(String code) {
        // Parse code like "DOG_GROWNUP_CARE_PLUS" or "CAT_ELDER_CARE"
        String upperCode = code.toUpperCase();

        boolean isPlus = upperCode.contains("PLUS");
        boolean isDog = upperCode.contains("DOG");
        boolean isCat = upperCode.contains("CAT");
        boolean isTimely = upperCode.contains("TIMELY");
        boolean isGrownup = upperCode.contains("GROWNUP") || upperCode.contains("GROWN_UP");
        boolean isElder = upperCode.contains("ELDER");
        boolean isActive = upperCode.contains("ACTIVE");

        // Determine package name
        String baseName;
        String ageGroup;
        BigDecimal basePrice;

        if (isTimely) {
            baseName = "Timely Care";
            ageGroup = isDog ? "PUPPY" : "KITTEN";
            basePrice = new BigDecimal("29.99");
        } else if (isElder) {
            baseName = "Elder Care";
            ageGroup = "SENIOR";
            basePrice = new BigDecimal("49.99");
        } else if (isActive) {
            baseName = "Active Care";
            ageGroup = "ADULT";
            basePrice = new BigDecimal("45.83"); // 550/12
        } else {
            baseName = "Grown-Up Care";
            ageGroup = "ADULT";
            basePrice = new BigDecimal("39.99");
        }

        if (isPlus) {
            baseName += " Plus";
            basePrice = basePrice.add(new BigDecimal("15.00"));
        }

        String petType = isDog ? "Dogs" : (isCat ? "Cats" : "Pets");
        String fullName = baseName + " for " + petType;

        // Build services list
        List<String> services = new ArrayList<>();
        services.add("Wellness Exams");
        services.add("Basic Vaccinations");

        if (isTimely) {
            services.add(isDog ? "Puppy Vaccines" : "Kitten Vaccines");
            services.add("Deworming");
        } else if (isElder) {
            services.add("Senior Blood Panel");
            services.add("Joint Health Check");
            services.add("Advanced Diagnostics");
        } else {
            services.add("Annual Diagnostics");
            services.add("Preventive Care");
        }

        if (isPlus) {
            services.add("Dental Cleaning");
            if (isTimely) {
                services.add("Spay/Neuter");
            }
        }

        Double valueScore = calculateValueScore(basePrice, services.size(), isPlus, isPlus && isTimely);

        return PackageComparison.builder()
                .packageCode(code)
                .packageName(fullName)
                .description("Comprehensive " + ageGroup.toLowerCase() + " pet care package")
                .monthlyPrice(basePrice)
                .annualPrice(basePrice.multiply(BigDecimal.valueOf(12)))
                .includedServices(services)
                .serviceCount(services.size())
                .includesDental(isPlus)
                .includesSpayNeuter(isPlus && isTimely)
                .targetAgeGroup(ageGroup)
                .careLevel(isPlus ? "PREMIUM" : "STANDARD")
                .valueScore(valueScore)
                .build();
    }

    private FeatureMatrix buildFeatureMatrix(List<PackageComparison> packages, List<String> focusFeatures) {
        Map<String, Map<String, Object>> features = new LinkedHashMap<>();
        List<String> packageOrder = packages.stream()
                .map(PackageComparison::getPackageCode)
                .collect(Collectors.toList());

        // Core features to compare
        List<String> allFeatures = new ArrayList<>(Arrays.asList(
                "Monthly Price",
                "Annual Price",
                "Services Included",
                "Dental Coverage",
                "Spay/Neuter Coverage",
                "Target Age Group",
                "Care Level",
                "Value Score"
        ));

        // Add focus features if specified
        if (focusFeatures != null && !focusFeatures.isEmpty()) {
            for (String focus : focusFeatures) {
                if (!allFeatures.contains(focus)) {
                    allFeatures.add(focus);
                }
            }
        }

        // Build matrix
        for (String feature : allFeatures) {
            Map<String, Object> packageValues = new LinkedHashMap<>();

            for (PackageComparison pkg : packages) {
                Object value = getFeatureValue(pkg, feature);
                packageValues.put(pkg.getPackageCode(), value);
            }

            features.put(feature, packageValues);
        }

        // Add service-by-service comparison
        Set<String> allServices = packages.stream()
                .flatMap(p -> p.getIncludedServices() != null ? p.getIncludedServices().stream() : java.util.stream.Stream.empty())
                .collect(Collectors.toSet());

        for (String service : allServices) {
            Map<String, Object> serviceAvailability = new LinkedHashMap<>();
            for (PackageComparison pkg : packages) {
                boolean hasService = pkg.getIncludedServices() != null &&
                        pkg.getIncludedServices().contains(service);
                serviceAvailability.put(pkg.getPackageCode(), hasService ? "✓" : "✗");
            }
            features.put(service, serviceAvailability);
            allFeatures.add(service);
        }

        return FeatureMatrix.builder()
                .features(features)
                .featureList(allFeatures)
                .packageOrder(packageOrder)
                .build();
    }

    private Object getFeatureValue(PackageComparison pkg, String feature) {
        return switch (feature) {
            case "Monthly Price" -> pkg.getMonthlyPrice() != null ?
                    "$" + pkg.getMonthlyPrice() : "N/A";
            case "Annual Price" -> pkg.getAnnualPrice() != null ?
                    "$" + pkg.getAnnualPrice() : "N/A";
            case "Services Included" -> pkg.getServiceCount();
            case "Dental Coverage" -> Boolean.TRUE.equals(pkg.getIncludesDental()) ? "✓" : "✗";
            case "Spay/Neuter Coverage" -> Boolean.TRUE.equals(pkg.getIncludesSpayNeuter()) ? "✓" : "✗";
            case "Target Age Group" -> pkg.getTargetAgeGroup() != null ?
                    pkg.getTargetAgeGroup() : "All";
            case "Care Level" -> pkg.getCareLevel() != null ? pkg.getCareLevel() : "Standard";
            case "Value Score" -> pkg.getValueScore() != null ?
                    String.format("%.1f/10", pkg.getValueScore() * 10) : "N/A";
            default -> "N/A";
        };
    }

    private PriceComparison buildPriceComparison(List<PackageComparison> packages) {
        Map<String, BigDecimal> monthlyPrices = new LinkedHashMap<>();
        Map<String, BigDecimal> annualPrices = new LinkedHashMap<>();
        Map<String, BigDecimal> savings = new LinkedHashMap<>();

        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;
        String cheapest = null;
        String mostExpensive = null;
        String bestValue = null;
        double bestValueScore = 0;

        BigDecimal totalPrice = BigDecimal.ZERO;
        int priceCount = 0;

        for (PackageComparison pkg : packages) {
            if (pkg.getMonthlyPrice() != null) {
                monthlyPrices.put(pkg.getPackageCode(), pkg.getMonthlyPrice());
                totalPrice = totalPrice.add(pkg.getMonthlyPrice());
                priceCount++;

                if (minPrice == null || pkg.getMonthlyPrice().compareTo(minPrice) < 0) {
                    minPrice = pkg.getMonthlyPrice();
                    cheapest = pkg.getPackageCode();
                }
                if (maxPrice == null || pkg.getMonthlyPrice().compareTo(maxPrice) > 0) {
                    maxPrice = pkg.getMonthlyPrice();
                    mostExpensive = pkg.getPackageCode();
                }
            }

            if (pkg.getAnnualPrice() != null) {
                annualPrices.put(pkg.getPackageCode(), pkg.getAnnualPrice());
            }

            if (pkg.getValueScore() != null && pkg.getValueScore() > bestValueScore) {
                bestValueScore = pkg.getValueScore();
                bestValue = pkg.getPackageCode();
            }

            // Calculate estimated savings vs individual services
            if (pkg.getMonthlyPrice() != null && pkg.getServiceCount() != null) {
                BigDecimal estimatedIndividual = BigDecimal.valueOf(pkg.getServiceCount() * 75); // $75 per service estimate
                BigDecimal packageAnnual = pkg.getAnnualPrice() != null ?
                        pkg.getAnnualPrice() : pkg.getMonthlyPrice().multiply(BigDecimal.valueOf(12));
                BigDecimal packageSavings = estimatedIndividual.subtract(packageAnnual);
                savings.put(pkg.getPackageCode(), packageSavings.max(BigDecimal.ZERO));
            }
        }

        BigDecimal avgPrice = priceCount > 0 ?
                totalPrice.divide(BigDecimal.valueOf(priceCount), 2, RoundingMode.HALF_UP) : null;
        BigDecimal priceDiff = (minPrice != null && maxPrice != null) ?
                maxPrice.subtract(minPrice) : null;

        return PriceComparison.builder()
                .cheapestPackage(cheapest)
                .mostExpensivePackage(mostExpensive)
                .priceDifference(priceDiff)
                .averagePrice(avgPrice)
                .bestValuePackage(bestValue)
                .monthlyPrices(monthlyPrices)
                .annualPrices(annualPrices)
                .savingsVsIndividual(savings)
                .build();
    }

    private ComparisonWinner determineWinner(List<PackageComparison> packages,
                                             List<String> priorities, Double maxBudget, String petType) {

        if (packages.isEmpty()) {
            return null;
        }

        // Score each package
        Map<String, Double> scores = new HashMap<>();

        for (PackageComparison pkg : packages) {
            double score = calculatePackageScore(pkg, priorities, maxBudget, petType);
            scores.put(pkg.getPackageCode(), score);
        }

        // Find winner
        String winnerCode = scores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        PackageComparison winner = packages.stream()
                .filter(p -> p.getPackageCode().equals(winnerCode))
                .findFirst()
                .orElse(packages.get(0));

        // Find category winners
        String bestBudget = packages.stream()
                .filter(p -> p.getMonthlyPrice() != null)
                .min(Comparator.comparing(PackageComparison::getMonthlyPrice))
                .map(PackageComparison::getPackageCode)
                .orElse(null);

        String bestCoverage = packages.stream()
                .max(Comparator.comparingInt(p -> p.getServiceCount() != null ? p.getServiceCount() : 0))
                .map(PackageComparison::getPackageCode)
                .orElse(null);

        String bestValuePkg = packages.stream()
                .filter(p -> p.getValueScore() != null)
                .max(Comparator.comparing(PackageComparison::getValueScore))
                .map(PackageComparison::getPackageCode)
                .orElse(null);

        // Build advantages and disadvantages
        List<String> advantages = buildAdvantages(winner, packages);
        List<String> disadvantages = buildDisadvantages(winner, packages);

        return ComparisonWinner.builder()
                .packageCode(winner.getPackageCode())
                .packageName(winner.getPackageName())
                .winReason(buildWinReason(winner, priorities))
                .advantages(advantages)
                .disadvantages(disadvantages)
                .matchScore(scores.get(winnerCode))
                .bestForBudget(bestBudget)
                .bestForCoverage(bestCoverage)
                .bestForValue(bestValuePkg)
                .build();
    }

    private double calculatePackageScore(PackageComparison pkg, List<String> priorities,
                                         Double maxBudget, String petType) {

        double score = 0.5; // Base score

        // Value score contribution
        if (pkg.getValueScore() != null) {
            score += pkg.getValueScore() * 0.3;
        }

        // Service count contribution
        if (pkg.getServiceCount() != null) {
            score += Math.min(pkg.getServiceCount() / 10.0, 0.2);
        }

        // Budget consideration
        if (maxBudget != null && pkg.getMonthlyPrice() != null) {
            if (pkg.getMonthlyPrice().doubleValue() <= maxBudget) {
                score += 0.1;
            } else {
                score -= 0.2; // Penalty for exceeding budget
            }
        }

        // Priority-based scoring
        if (priorities != null) {
            for (String priority : priorities) {
                switch (priority.toLowerCase()) {
                    case "price", "budget", "cheap" -> {
                        if (pkg.getMonthlyPrice() != null &&
                                pkg.getMonthlyPrice().compareTo(new BigDecimal("40")) < 0) {
                            score += 0.15;
                        }
                    }
                    case "dental" -> {
                        if (Boolean.TRUE.equals(pkg.getIncludesDental())) {
                            score += 0.2;
                        }
                    }
                    case "comprehensive", "coverage" -> {
                        if (pkg.getServiceCount() != null && pkg.getServiceCount() > 5) {
                            score += 0.15;
                        }
                    }
                    case "value" -> {
                        if (pkg.getValueScore() != null && pkg.getValueScore() > 0.7) {
                            score += 0.15;
                        }
                    }
                }
            }
        }

        // Pet type match (if package code contains pet type)
        if (petType != null && pkg.getPackageCode() != null &&
                pkg.getPackageCode().toUpperCase().contains(petType.toUpperCase())) {
            score += 0.1;
        }

        return Math.min(score, 1.0);
    }

    private List<String> buildAdvantages(PackageComparison winner, List<PackageComparison> allPackages) {
        List<String> advantages = new ArrayList<>();

        // Check if cheapest
        boolean isCheapest = allPackages.stream()
                .filter(p -> p.getMonthlyPrice() != null && winner.getMonthlyPrice() != null)
                .noneMatch(p -> !p.getPackageCode().equals(winner.getPackageCode()) &&
                        p.getMonthlyPrice().compareTo(winner.getMonthlyPrice()) < 0);
        if (isCheapest && winner.getMonthlyPrice() != null) {
            advantages.add("Most affordable option at $" + winner.getMonthlyPrice() + "/month");
        }

        // Check if most services
        boolean hasMostServices = allPackages.stream()
                .noneMatch(p -> !p.getPackageCode().equals(winner.getPackageCode()) &&
                        p.getServiceCount() != null && winner.getServiceCount() != null &&
                        p.getServiceCount() > winner.getServiceCount());
        if (hasMostServices && winner.getServiceCount() != null && winner.getServiceCount() > 2) {
            advantages.add("Includes " + winner.getServiceCount() + " services - most comprehensive");
        }

        // Dental coverage
        if (Boolean.TRUE.equals(winner.getIncludesDental())) {
            advantages.add("Includes dental coverage");
        }

        // Spay/Neuter
        if (Boolean.TRUE.equals(winner.getIncludesSpayNeuter())) {
            advantages.add("Includes spay/neuter coverage");
        }

        // Best value
        if (winner.getValueScore() != null && winner.getValueScore() > 0.7) {
            advantages.add("Excellent value for money");
        }

        return advantages;
    }

    private List<String> buildDisadvantages(PackageComparison winner, List<PackageComparison> allPackages) {
        List<String> disadvantages = new ArrayList<>();

        // Check if most expensive
        boolean isMostExpensive = allPackages.stream()
                .filter(p -> p.getMonthlyPrice() != null && winner.getMonthlyPrice() != null)
                .noneMatch(p -> !p.getPackageCode().equals(winner.getPackageCode()) &&
                        p.getMonthlyPrice().compareTo(winner.getMonthlyPrice()) > 0);
        if (isMostExpensive && allPackages.size() > 1 && winner.getMonthlyPrice() != null) {
            disadvantages.add("Higher price point than alternatives");
        }

        // Missing dental
        if (!Boolean.TRUE.equals(winner.getIncludesDental())) {
            boolean othersHaveDental = allPackages.stream()
                    .anyMatch(p -> Boolean.TRUE.equals(p.getIncludesDental()));
            if (othersHaveDental) {
                disadvantages.add("Does not include dental coverage (available in other packages)");
            }
        }

        // Missing spay/neuter
        if (!Boolean.TRUE.equals(winner.getIncludesSpayNeuter())) {
            boolean othersHaveSpay = allPackages.stream()
                    .anyMatch(p -> Boolean.TRUE.equals(p.getIncludesSpayNeuter()));
            if (othersHaveSpay) {
                disadvantages.add("Spay/neuter not included");
            }
        }

        return disadvantages;
    }

    private String buildWinReason(PackageComparison winner, List<String> priorities) {
        StringBuilder reason = new StringBuilder();
        reason.append(winner.getPackageName()).append(" offers the best overall balance");

        if (priorities != null && !priorities.isEmpty()) {
            reason.append(" considering your priorities: ").append(String.join(", ", priorities));
        }

        if (winner.getValueScore() != null && winner.getValueScore() > 0.7) {
            reason.append(". It provides excellent value for money");
        }

        return reason.toString();
    }

    private String generateSummary(List<PackageComparison> packages, ComparisonWinner winner,
                                   ComparisonRequest request) {

        StringBuilder summary = new StringBuilder();
        summary.append("Comparing ").append(packages.size()).append(" packages: ");
        summary.append(packages.stream()
                .map(p -> p.getPackageName() != null ? p.getPackageName() : p.getPackageCode())
                .collect(Collectors.joining(", ")));
        summary.append(".\n\n");

        if (winner != null) {
            summary.append("**Recommended: ").append(winner.getPackageName()).append("**\n");
            summary.append(winner.getWinReason()).append(".\n\n");

            if (winner.getBestForBudget() != null && !winner.getBestForBudget().equals(winner.getPackageCode())) {
                summary.append("For budget-conscious options, consider ").append(winner.getBestForBudget()).append(".\n");
            }
            if (winner.getBestForCoverage() != null && !winner.getBestForCoverage().equals(winner.getPackageCode())) {
                summary.append("For maximum coverage, consider ").append(winner.getBestForCoverage()).append(".\n");
            }
        }

        return summary.toString();
    }

    private List<String> identifyKeyDifferences(List<PackageComparison> packages, FeatureMatrix matrix) {
        List<String> differences = new ArrayList<>();

        // Price range
        BigDecimal minPrice = packages.stream()
                .filter(p -> p.getMonthlyPrice() != null)
                .map(PackageComparison::getMonthlyPrice)
                .min(BigDecimal::compareTo)
                .orElse(null);
        BigDecimal maxPrice = packages.stream()
                .filter(p -> p.getMonthlyPrice() != null)
                .map(PackageComparison::getMonthlyPrice)
                .max(BigDecimal::compareTo)
                .orElse(null);

        if (minPrice != null && maxPrice != null && !minPrice.equals(maxPrice)) {
            differences.add("Price range: $" + minPrice + " to $" + maxPrice + " per month");
        }

        // Dental coverage difference
        long withDental = packages.stream()
                .filter(p -> Boolean.TRUE.equals(p.getIncludesDental()))
                .count();
        if (withDental > 0 && withDental < packages.size()) {
            differences.add(withDental + " of " + packages.size() + " packages include dental coverage");
        }

        // Service count variation
        int minServices = packages.stream()
                .filter(p -> p.getServiceCount() != null)
                .mapToInt(PackageComparison::getServiceCount)
                .min()
                .orElse(0);
        int maxServices = packages.stream()
                .filter(p -> p.getServiceCount() != null)
                .mapToInt(PackageComparison::getServiceCount)
                .max()
                .orElse(0);

        if (minServices != maxServices) {
            differences.add("Service count ranges from " + minServices + " to " + maxServices);
        }

        // Age group variation
        Set<String> ageGroups = packages.stream()
                .map(PackageComparison::getTargetAgeGroup)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (ageGroups.size() > 1) {
            differences.add("Packages target different age groups: " + String.join(", ", ageGroups));
        }

        return differences;
    }

    private double calculateConfidence(List<PackageComparison> packages) {
        // Base confidence
        double confidence = 0.7;

        // Increase if we have complete data
        long completePackages = packages.stream()
                .filter(p -> p.getMonthlyPrice() != null &&
                        p.getPackageName() != null &&
                        p.getIncludedServices() != null &&
                        !p.getIncludedServices().isEmpty())
                .count();

        confidence += (completePackages / (double) packages.size()) * 0.2;

        // Bonus for having more packages to compare
        if (packages.size() >= 3) {
            confidence += 0.05;
        }

        return Math.min(confidence, 0.95);
    }

    private Double calculateValueScore(BigDecimal monthlyPrice, int serviceCount,
                                       Boolean includesDental, Boolean includesSpayNeuter) {
        if (monthlyPrice == null || monthlyPrice.compareTo(BigDecimal.ZERO) == 0) {
            return 0.5;
        }

        // Base value = services per dollar (normalized)
        double priceValue = monthlyPrice.doubleValue();
        double servicesPerDollar = serviceCount / priceValue;
        double baseScore = Math.min(servicesPerDollar * 5, 0.7);

        // Bonus for premium features
        if (Boolean.TRUE.equals(includesDental)) {
            baseScore += 0.15;
        }
        if (Boolean.TRUE.equals(includesSpayNeuter)) {
            baseScore += 0.1;
        }

        return Math.min(baseScore, 1.0);
    }

    // ==================== Utility Methods ====================

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) return null;
        if (value instanceof BigDecimal) return (BigDecimal) value;
        if (value instanceof Number) return new BigDecimal(value.toString());
        try {
            return new BigDecimal(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Boolean toBoolean(Object value) {
        if (value == null) return false;
        if (value instanceof Boolean) return (Boolean) value;
        return Boolean.parseBoolean(value.toString());
    }

    private String toString(Object value) {
        if (value == null) return null;
        return value.toString();
    }
}