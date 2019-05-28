var express = require('express');
var router = express.Router();

router.get('/', function(req, res){
    const ip = req.headers['x-forwarded-for'] ||  req.connection.remoteAddress;
    console.log('ip:'+ip);
    res.send('This is PetComm, Welcome World!');
});

router.post('/', function(req, res){
    const ip = req.headers['x-forwarded-for'] ||  req.connection.remoteAddress;
    var data1 = req.body.data1;
    var data2 = req.body.data2;
    console.log('post connected complete in :'+ip);
    console.log(data1);
    console.log(data2);
    res.status(200).json({
        message: 'data1 is:' + data1 + '  data2 is:' + data2
    });
});

module.exports = router;