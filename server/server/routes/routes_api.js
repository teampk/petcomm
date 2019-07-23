var express = require('express');
var router = express.Router();
var registerDog = require('../functions/registerDog');

router.get('/', function(req, res) {
    res.end('This is for pk server');
});

router.post('/dogs', function(req, res){
    var dogName = req.body.name;
    var dogGender = req.body.gender;
    var dogBreeds = req.body.breeds;
    var dogBirth = req.body.birth;
    var dogWeight = req.body.weight;
    var userEmail = req.body.email;
    var feederId = req.body.feederId;
    var toiletId = req.body.toiletId;
    console.log('--register start--');
    console.log(dogName+'//'+dogGender+'//'+dogBreeds+'//'+dogBirth+'//'+dogWeight+'//'+userEmail+'//'+feederId+'//'+toiletId+'//')

    if (!dogName || !dogGender || !dogBreeds || !dogBirth || !dogWeight || !userEmail || !dogName.trim() || !dogGender.trim() || !dogBreeds.trim() || !dogBirth.trim() || !dogWeight.trim() || !userEmail.trim()){
        res.status(400).json({
          message: 'Invalid Request !'
        });
    }else{
        registerDog.registerDog(dogName, dogGender, dogBreeds, dogBirth, dogWeight, userEmail, feederId, toiletId)
        .then(function(result) {
            // res.setHeader('Location', '/dogs/' + ownerId);
            res.status(result.status).json({
              message: result.message
            });
          })
          .catch(function(err) {
            res.status(err.status).json({
              message: err.message
            });
    
          });
    
    }
});

module.exports = router;