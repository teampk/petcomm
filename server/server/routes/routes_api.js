var express = require('express');
var router = express.Router();
var registerDog = require('../functions/registerDog');

router.get('/', function(req, res) {
    res.end('This is for pk server');
});

router.post('/dogs', function(req, res){
    var dogId = req.body.dogId;
    var dogName = req.body.dogName;
    var dogGender = req.body.dogGender;
    var dogBreeds = req.body.dogBreeds;
    var dogBirth = req.body.dogBirth;
    var dogWeight = req.body.dogWeight;
    var userEmail = req.body.userEmail;
    var feederId = req.body.feederId;
    var toiletId = req.body.toiletId;
    console.log('--register start--');

    if (!dogId || !dogName || !dogGender || !dogBreeds || !dogBirth || !dogWeight || !userEmail || !feederId || !toiletId || !dogName.trim() || !dogGender.trim() || !dogBreeds.trim() || !dogBirth.trim() || !dogWeight.trim() || !userEmail.trim() || !feederId.trim() || !toiletId.trim()){
        res.status(400).json({
          message: 'Invalid Request !'
        });
    }else{
        registerDog.registerDog(dogId, dogName, dogGender, dogBreeds, dogBirth, dogWeight, userEmail, feederId, toiletId)
        .then(function(result) {
            console.log('post result: ' + result);
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