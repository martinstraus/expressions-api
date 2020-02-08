elements = {
    code: document.getElementById("code"),
    result: document.getElementById("result")
};

function evaluate() {
    var request = new XMLHttpRequest();
    request.onreadystatechange = () => interpretResponse(request);
    request.open('POST', '/expressions/evaluate', true);
    request.setRequestHeader('Content-Type', 'application/json');
    request.send(evaluateBody());
}

function interpretResponse(request) {
    if (request.readyState === XMLHttpRequest.DONE) {
        updateResult(JSON.parse(request.responseText));
    }
}
function updateResult(result) {
    elements.result.value = result[0].result;
}

function evaluateBody() {
    return JSON.stringify(request());
}

function request() {
    return {
        expressions: [
            expression()
        ],
        parameters: {}
    };
}

function expression() {
    return code.value;
}

function evaluateExpression(event) {
    event.preventDefault();
    evaluate();
    return false;
}