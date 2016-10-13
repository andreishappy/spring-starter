var express = require('express');
var bodyParser = require('body-parser');
var fs = require('fs');
var colors = require('colors');
var app = express();
var sleep = require('sleep');

app.use(bodyParser.json()); // for parsing application/json

function queryMatch(a, b) {
  a = a || {};
  b = b || {};

  if (a === b) {
    return true;
  }

  for (var property in a) {
    if (a[property] !== b[property]) {
      return false;
    }
  }
  return true;
}

function bodyMatch(configuredRequest, currentRequest) {
    configuredRequest = configuredRequest || {};
    currentRequest = currentRequest || {};
    return JSON.stringify(configuredRequest) === JSON.stringify(currentRequest);
}

function matchRequest(req, callback, debug) {
  var match = dictionary.filter(x => {
    var pathMatches = x.request.path === req.path;
    var methodMatches = x.request.method === req.method;
    var queryMatches = queryMatch(x.request.query, req.query);
    var bodyMatches = bodyMatch(x.request.body, req.body)
    var matches = pathMatches && methodMatches && queryMatches && bodyMatches

    if (debug) {
      console.log(`Match -> ${matches}`.yellow)
      console.log(`${x.request.path} VS ${req.path} -> ${pathMatches}`);
      console.log(`${x.request.method} VS ${req.method} -> ${methodMatches}`);
      console.log(`${JSON.stringify(x.request.body)} VS ${JSON.stringify(req.body)} -> ${bodyMatches}`)
      console.log(`${JSON.stringify(x.request.query)} VS ${JSON.stringify(req.query)} -> ${queryMatches}`);
    }

    return matches;
  });

  callback = callback || ((data, error) => { });
  if (match.length === 0) {
    callback(null, 'match.length = 0');
    return null;
  } else if (match.length > 1) {
    callback(null, `match.length = ${match.length}`);
    return null;
  }

  callback(match[0], null);
  return match[0];
}
/*
  {
    "request": {
      "path": "/example",
      "method": "GET",
      "query": {
        "foo": "bar"
      }
    },
    "response": {
      "file": "example.json",
      "status": 200
    }
  }
*/
var dictionary = [];
var calls = [];

app.get('/configure/get-calls', (req, res) => {
  res.status(200).json(calls);
});

app.post('/configure/clean', (req, res) => {
  // console.log('/configure/clean'.green);
  dictionary = [];
  calls = [];
  res.sendStatus(200);
});

app.post('/configure/clean-calls', (req, res) => {
  // console.log('/configure/clean/calls'.green);
  calls = [];
  res.sendStatus(200);
});

app.post('/configure', (req, res, params) => {
   console.log('/configure'.green);
   console.log(req.body);

  var match = matchRequest({
    path: req.body.request.path,
    method: req.body.request.method,
    query: req.body.request.query,
    body: req.body.request.body
  });

  if (match) {
    res.sendStatus(200);
  } else {
    dictionary.push(req.body);
    res.sendStatus(201);
  }
});

app.all(/^\/?(?!configure).*$/, (req, res) => {
  console.log(req)

  var path = req.path || '<no path>'

  console.log(path.blue);
  calls.push({
     path: req.path,
     method: req.method,
     query: req.query,
     headers: req.headers
  });

  var match = matchRequest(req);

  if (!match) {
    matchRequest(req, null, true);
    console.log('no match found'.red);
    res.sendStatus(500);
    return;
  }

  if (match.response.file !== null) {
    var file = `./responses/${match.response.file}`;

    fs.readFile(file, (err, data) => {
      // console.log(`reading ${file}`);
      if (err) {
      // console.log(`error reading ${file}: ${err}`)
        res.status(500).send(`Error reading ${file}`);
        return;
      }
        // console.log(data);
      res.status(match.response.status).send(JSON.parse(data));
    })
  } else {
    res.status(match.response.status).end();
  }
});

app.listen(3000, () => { console.log('Listening on 3000') });
