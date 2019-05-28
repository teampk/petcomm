var express = require('express');
var router = express.Router();

router.get('/', function(req, res){
    const ip = req.headers['x-forwarded-for'] ||  req.connection.remoteAddress;
    console.log('ip:'+ip);
    res.send('This is PetComm, Welcome World!');
});

router.post('/', function(req, res){
    var data = req.body.data;
    res.send('data', data);
});

module.exports = router;