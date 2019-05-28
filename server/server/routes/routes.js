var express = require('express');
var router = express.Router();

router.get('/', function(req, res){
    const ip = req.headers['x-forwarded-for'] ||  req.connection.remoteAddress;
    console.log('ip:'+ip);
    res.send('This is PetComm, Welcome World!');
});

router.post('/', function(req, res){
    const ip = req.headers['x-forwarded-for'] ||  req.connection.remoteAddress;
    var data = req.body.data;
    console.log('post connected complete in :'+ip);
    console.log(data);
    if(data!=undefined){
        res.send('data', data);
    }else{
        res.send('Data empty')
    }
});

module.exports = router;