echo "Adding v1/kv/moviebuzz-api/develop/moviebuzz-api.yml"
curl -X PUT --data-binary @moviebuzz-api-develop.yml -H "Content-type: text/x-yaml" http://consul:8500/v1/kv/moviebuzz-api/develop/moviebuzz-api.yml
echo "Container Initialization Complete!"