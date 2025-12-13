#!/bin/bash

# ============================================
# Comparison Agent API Test Script
# ============================================

BASE_URL="http://localhost:8083/api/v1/agents/comparison"

echo "============================================"
echo "  Comparison Agent API Tests"
echo "============================================"

echo ""
echo ""
echo "Test 1: Quick comparison - Two dog packages"
# Test 1: Quick comparison of two packages
curl -s "${BASE_URL}/quick?package1=DOG_GROWNUP_CARE&package2=DOG_GROWNUP_CARE_PLUS"
echo ""

echo ""
echo ""
echo "Test 2: Quick comparison - Base vs Plus"
# Test 2: Compare base and plus versions
curl -s "${BASE_URL}/quick?package1=DOG_TIMELY_CARE&package2=DOG_TIMELY_CARE_PLUS"
echo ""

echo ""
echo ""
echo "Test 3: Multiple packages comparison"
# Test 3: Compare multiple packages
curl -s "${BASE_URL}/multiple?packages=DOG_TIMELY_CARE&packages=DOG_GROWNUP_CARE&packages=DOG_ELDER_CARE"
echo ""

echo ""
echo ""
echo "Test 4: Full comparison with priorities"
# Test 4: Full comparison with dental priority
curl -s -X POST "${BASE_URL}/compare" \
  -H "Content-Type: application/json" \
  -d '{
    "packageCodes": ["DOG_GROWNUP_CARE", "DOG_GROWNUP_CARE_PLUS"],
    "priorities": ["dental", "value"]
  }'
echo ""

echo ""
echo ""
echo "Test 5: Full comparison with budget constraint"
# Test 5: Comparison with budget limit
curl -s -X POST "${BASE_URL}/compare" \
  -H "Content-Type: application/json" \
  -d '{
    "packageCodes": ["DOG_TIMELY_CARE", "DOG_GROWNUP_CARE", "DOG_ELDER_CARE"],
    "maxBudget": 45.00,
    "priorities": ["budget"]
  }'
echo ""

echo ""
echo ""
echo "Test 6: Focus comparison - Dental focus"
# Test 6: Compare with focus on specific features
curl -s -X POST "${BASE_URL}/focus?packages=DOG_GROWNUP_CARE&packages=DOG_GROWNUP_CARE_PLUS&focus=dental&focus=vaccinations"
echo ""

echo ""
echo ""
echo "Test 7: Pet-specific comparison - Adult dog"
# Test 7: Compare for specific pet type and age
curl -s "${BASE_URL}/for-pet?packages=DOG_GROWNUP_CARE&packages=DOG_GROWNUP_CARE_PLUS&packages=DOG_ELDER_CARE&petType=DOG&ageYears=5"
echo ""

echo ""
echo ""
echo "Test 8: Pet-specific comparison - Senior cat"
# Test 8: Senior cat comparison
curl -s "${BASE_URL}/for-pet?packages=CAT_GROWNUP_CARE&packages=CAT_ELDER_CARE&packages=CAT_ELDER_CARE_PLUS&petType=CAT&ageYears=12"
echo ""

echo ""
echo ""
echo "Test 9: Full comparison - All puppy packages"
# Test 9: Compare all puppy/kitten packages
curl -s -X POST "${BASE_URL}/compare" \
  -H "Content-Type: application/json" \
  -d '{
    "packageCodes": ["DOG_TIMELY_CARE", "DOG_TIMELY_CARE_PLUS"],
    "petType": "DOG",
    "petAgeYears": 0,
    "petAgeMonths": 6
  }'
echo ""

echo ""
echo ""
echo "Test 10: Full comparison with session and user query"
# Test 10: Full featured comparison
curl -s -X POST "${BASE_URL}/compare" \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "session-xyz-789",
    "packageCodes": ["DOG_GROWNUP_CARE", "DOG_GROWNUP_CARE_PLUS", "DOG_ELDER_CARE"],
    "petType": "DOG",
    "petAgeYears": 6,
    "priorities": ["comprehensive", "dental"],
    "focusFeatures": ["dental", "diagnostics"],
    "userQuery": "Which package is best for my 6 year old dog who needs dental care?"
  }'
echo ""

echo ""
echo ""
echo "Test 11: Compare Cat packages"
# Test 11: Cat packages comparison
curl -s "${BASE_URL}/quick?package1=CAT_GROWNUP_CARE&package2=CAT_GROWNUP_CARE_PLUS"
echo ""

echo ""
echo ""
echo "Test 12: Four packages comparison (max)"
# Test 12: Maximum packages comparison
curl -s -X POST "${BASE_URL}/compare" \
  -H "Content-Type: application/json" \
  -d '{
    "packageCodes": ["DOG_TIMELY_CARE", "DOG_GROWNUP_CARE", "DOG_GROWNUP_CARE_PLUS", "DOG_ELDER_CARE"]
  }'
echo ""

echo ""
echo "============================================"
echo "  Tests Complete!"
echo "============================================"