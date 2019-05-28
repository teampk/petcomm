var express = require('express');
var router = express.Router();

router.get('/', function(req, res){
    res.send('this is user page');
});

router.post('/', function(req, res){
    var data = req.body.data;
    res.send('data', data);
});

module.exports = router;