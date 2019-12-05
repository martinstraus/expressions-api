# expressions-api
API for configuring and running expressions.

## Examples

This...

    curl -X POST http://localhost:8080/expressions/evaluate \
        -H "Content-Type: application/json" \
        -d '{
            "expressions": [
                "a+b",
                "c = \"hello, world!\""
            ],
            "parameters": {
                "a": 1,
                "b": 2,
                "c": "hello, world!"
            }
        }'

... will return this...

    [{"result":3,"expression":"a+b"},{"result":true,"expression":"c = \"hello, world!\""}]