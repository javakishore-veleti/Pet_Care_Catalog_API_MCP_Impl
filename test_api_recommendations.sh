#!/bin/bash

# ============================================
# Recommendation Agent API Test Script
# ============================================

BASE_URL="http://localhost:8083/api/v1/agents/recommendation"

echo "============================================"
echo "  Recommendation Agent API Tests"
echo "============================================"

echo ""
echo ""
echo "Test 1: Quick recommendation - Adult Dog"
# Test 1: Quick recommendation for adult dog
curl -s "${BASE_URL}/quick?petType=DOG&ageYears=3"
echo ""

echo ""
echo ""
echo "Test 2: Quick recommendation - Puppy"
# Test 2: Quick recommendation for puppy
curl -s "${BASE_URL}/quick?petType=DOG&ageYears=0&ageMonths=6"
echo ""

echo ""
echo ""
echo "Test 3: Quick recommendation - Senior Cat"
# Test 3: Quick recommendation for senior cat
curl -s "${BASE_URL}/quick?petType=CAT&ageYears=12"
echo ""

echo ""
echo ""
echo "Test 4: Full recommendation - Dog with dental needs"
# Test 4: Full recommendation with dental care
curl -s -X POST "${BASE_URL}/recommend" \
  -H "Content-Type: application/json" \
  -d '{
    "petType": "DOG",
    "petName": "Buddy",
    "ageYears": 3,
    "ageMonths": 6,
    "needsDentalCare": true
  }'
echo ""

echo ""
echo ""
echo "Test 5: Full recommendation - Cat with vaccinations"
# Test 5: Full recommendation with vaccination needs
curl -s -X POST "${BASE_URL}/recommend" \
  -H "Content-Type: application/json" \
  -d '{
    "petType": "CAT",
    "petName": "Whiskers",
    "ageYears": 2,
    "ageMonths": 0,
    "needsVaccinations": true
  }'
echo ""

echo ""
echo ""
echo "Test 6: Full recommendation - Senior Dog with chronic conditions"
# Test 6: Senior dog with chronic conditions
curl -s -X POST "${BASE_URL}/recommend" \
  -H "Content-Type: application/json" \
  -d '{
    "petType": "DOG",
    "petName": "Max",
    "ageYears": 10,
    "ageMonths": 0,
    "hasChronicConditions": true,
    "existingConditions": ["arthritis", "diabetes"],
    "needsDentalCare": true
  }'
echo ""

echo ""
echo ""
echo "Test 7: Full recommendation - Kitten"
# Test 7: Kitten recommendation
curl -s -X POST "${BASE_URL}/recommend" \
  -H "Content-Type: application/json" \
  -d '{
    "petType": "CAT",
    "petName": "Luna",
    "ageYears": 0,
    "ageMonths": 4,
    "needsVaccinations": true
  }'
echo ""

echo ""
echo ""
echo "Test 8: Full recommendation - Budget conscious"
# Test 8: Budget conscious recommendation
curl -s -X POST "${BASE_URL}/recommend" \
  -H "Content-Type: application/json" \
  -d '{
    "petType": "DOG",
    "petName": "Rocky",
    "ageYears": 5,
    "ageMonths": 0,
    "budgetPreference": "BASIC"
  }'
echo ""

echo ""
echo ""
echo "Test 9: Full recommendation - Premium/Comprehensive"
# Test 9: Premium comprehensive recommendation
curl -s -X POST "${BASE_URL}/recommend" \
  -H "Content-Type: application/json" \
  -d '{
    "petType": "DOG",
    "petName": "Duke",
    "ageYears": 4,
    "ageMonths": 0,
    "needsDentalCare": true,
    "needsVaccinations": true,
    "budgetPreference": "PREMIUM",
    "preferComprehensive": true
  }'
echo ""

echo ""
echo ""
echo "Test 10: Refine recommendation"
# Test 10: Refine an existing recommendation
curl -s -X POST "${BASE_URL}/refine/session-12345" \
  -H "Content-Type: application/json" \
  -d 'I have a senior dog that also needs dental care and has joint problems'
echo ""

echo ""
echo ""
echo "Test 11: Full recommendation with session ID"
# Test 11: With session tracking
curl -s -X POST "${BASE_URL}/recommend" \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "session-abc-123",
    "petType": "CAT",
    "petName": "Mittens",
    "ageYears": 7,
    "ageMonths": 0,
    "needsDentalCare": false,
    "userQuery": "What package is best for my adult cat?"
  }'
echo ""

echo ""
echo "============================================"
echo "  Tests Complete!"
echo "============================================"