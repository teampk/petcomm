var express = require('express');
var router = express.Router();
var functionDog = require('../functions/functionDog');

router.get('/', function(req, res) {
    res.end('This is for pk server');
});

// Register Dog
router.post('/dog', function(req, res){
    var dogId = req.body.dogId;
    var dogName = req.body.dogName;
    var dogGender = req.body.dogGender;
    var dogBreeds = req.body.dogBreeds;
    var dogBirth = req.body.dogBirth;
    var dogWeight = req.body.dogWeight;
    var userEmail = req.body.userEmail;
    var feederId = req.body.feederId;
    var toiletId = req.body.toiletId;
    console.log('-- Register Start --');
    console.log(dogId+"//"+dogName+'//'+dogGender+'//'+dogBreeds+'//'+dogBirth+'//'+dogWeight+'//'+userEmail+'//'+feederId+'//'+toiletId+'//')

    if (!dogId || !dogName || !dogGender || !dogBreeds || !dogBirth || !dogWeight || !userEmail || !dogName.trim() || !dogGender.trim() || !dogBreeds.trim() || !dogBirth.trim() || !dogWeight.trim() || !userEmail.trim()){
        res.status(400).json({
          message: 'Invalid Request !'
        });
    }else{
      functionDog.registerDog(dogId, dogName, dogGender, dogBreeds, dogBirth, dogWeight, userEmail, feederId, toiletId)
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

router.get('/dogs/:id', function(req, res){
  console.log("user id = " + req.params.id);
  functionDog.getDogsById(req.params.id)
    .then(function(result){
      console.log('dog result : ' + result);
      res.json(result);
    })
    .catch(function(err){
      console.log('dog loading err : ' + err);
      res.status(err.status).json({
        message : err.message
      });
    });

});

router.get('/dog/:id', function(req, res){
  functionDog.getDogBydogId(req.params.id)
    .then(function(result){
      console.log('dogID = ' + req.params.id);
      res.json(result);
    })
    .catch(function(err){
      console.log('dog err : '+err);
      res.status(err.status).json({
        message: err.message
      });
    });
});

module.exports = router;