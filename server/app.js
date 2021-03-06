var express =require('express');
var path = require('path');
var bodyParser = require('body-parser');
var logger = require('morgan');
var mongoose = require('mongoose');

var routes = require('./server/routes/routes');
var routes_api = require('./server/routes/routes_api');
var routes_device = require('./server/routes/routes_device');

var app = express();
var router = express.Router();

app.use(bodyParser.json());

//app.use(express.bodyParser());

app.use(logger('petcomm'));

mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:27017/petcomm', {useNewUrlParser: true});

app.use('/', routes);
app.use('/api/pk', routes_api);
app.use('/device/pk', routes_device);

module.exports = app;

var server = app.listen(8080, function(){
    console.log('Express Server Listening on Port 8080');
})