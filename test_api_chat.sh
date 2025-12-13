echo ""
echo ""
echo "Test 1: Health check"
# Test 1: Health check
curl http://localhost:8083/api/chat/health
echo ""

echo ""
echo ""
echo "Test 2: Info"
# Test 2: Info
curl http://localhost:8083/api/chat
echo ""

echo ""
echo ""
echo "Test 3: Simple chat - Help message"
# Test 3: Simple chat - Help message
curl -X POST http://localhost:8083/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "hello"
  }'
echo ""

echo ""
echo ""
echo "Test 4: Search for dog packages"
# Test 4: Search for dog packages
curl -X POST http://localhost:8083/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "search dog packages"
  }'
echo ""

echo ""
echo ""
echo "Test 5: Get package details"
# Test 5: Get package details
curl -X POST http://localhost:8083/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "details about DOG_ACTIVE_CARE"
  }'
echo ""

echo ""
echo ""
echo "Test 6: Get services"
# Test 6: Get services
curl -X POST http://localhost:8083/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "show me services"
  }'

echo ""
echo ""
echo "Test 7: Recommendation"
# Test 7: Recommendation
curl -X POST http://localhost:8083/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "recommend package for 3 year old dog"
  }'