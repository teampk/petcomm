var express = require('express');
var router = express.Router();
var functionDog = require('../functions/functionDog');

router.get('/', function(req, res) {
    res.end('This is for pk server');
});

// Register Dog
router.post('/dog', function(req, res){
    var dogName = req.body.name;
    var dogGender = req.body.gender;
    var dogBreeds = req.body.breeds;
    var dogBirth = req.body.birth;
    var dogWeight = req.body.weight;
    var userEmail = req.body.email;
    var feederId = req.body.feederId;
    var toiletId = req.body.toiletId;
    console.log('-- Register Start --');
    console.log(dogName+'//'+dogGender+'//'+dogBreeds+'//'+dogBirth+'//'+dogWeight+'//'+userEmail+'//'+feederId+'//'+toiletId+'//')

    if (!dogName || !dogGender || !dogBreeds || !dogBirth || !dogWeight || !userEmail || !dogName.trim() || !dogGender.trim() || !dogBreeds.trim() || !dogBirth.trim() || !dogWeight.trim() || !userEmail.trim()){
        res.status(400).json({
          message: 'Invalid Request !'
        });
    }else{
      functionDog.registerDog(dogName, dogGender, dogBreeds, dogBirth, dogWeight, userEmail, feederId, toiletId)
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
  functionDog.loadDogs(req.params.id)
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

module.exports = router;