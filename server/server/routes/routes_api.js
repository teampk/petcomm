var express = require('express');
var router = express.Router();
var functionDog = require('../functions/functionDog');
var functionSchedule = require('../functions/functionAutoSchedule');
var request = require('request');

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
  var feederId = "";
  var toiletId = "";
  var feederIP = "";
  var toiletIP = "";
  console.log('-- Register Start --');
  console.log(dogId+"//"+dogName+'//'+dogGender+'//'+dogBreeds)

  if (!dogId || !dogName || !dogGender || !dogBreeds || !dogBirth || !dogWeight || !userEmail || !dogName.trim() || !dogGender.trim() || !dogBreeds.trim() || !dogBirth.trim() || !dogWeight.trim() || !userEmail.trim()){
      res.status(400).json({
        message: 'Invalid Request !'
      });
  }else{
    functionDog.registerDog(dogId, dogName, dogGender, dogBreeds, dogBirth, dogWeight, userEmail, feederId, toiletId, feederIP, toiletIP)
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

// get Dogs list by user id
router.get('/dogs/:id', function(req, res){
  console.log("user id = " + req.params.id);
  functionDog.getDogsById(req.params.id)
    .then(function(result){
      console.log('-- Getting Dog List...--');
      res.json(result);
    })
    .catch(function(err){
      console.log('dog loading err : ' + err);
      res.status(err.status).json({
        message : err.message
      });
    });

});



// get dog profile by dog id
router.get('/dog/:dogId', function(req, res){
  functionDog.getDogBydogId(req.params.dogId)
    .then(function(result){
      console.log('-- Getting Dog Profile... --');
      console.log('dogID = ' + req.params.dogId);
      res.json(result);
    })
    .catch(function(err){
      console.log('dog err : '+err);
      res.status(err.status).json({
        message: err.message
      });
    });
});


// register 할 때 앱으로부터 기기 이름도 받아와서 저장하자
// register Feeder
router.put('/register/feeder/:dogId', function(req, res){
  functionDog.setDeviceId(req.params.dogId, 1)
    .then(function(result){
      res.status(result.status).json({
        message: result.message
      });
    })
    .catch(function(err){
      console.log('FeederRegisterERROR:'+err);
      res.status(err.status).json({
        message: err.message
      });
    });
});

// register Toilet
router.put('/register/toilet/:dogId', function(req, res){
  functionDog.setDeviceId(req.params.dogId, 2)
    .then(function(result){
      res.status(result.status).json({
        message: result.message
      });
    })
    .catch(function(err){
      console.log('ToiletRegisterERROR:'+err);
      res.status(err.status).json({
        message: err.message
      });
    });
});

// unregister Feeder
router.put('/unregister/feeder/:dogId', function(req, res){
  functionDog.setDeviceId(req.params.dogId, 11)
    .then(function(result){
      res.status(result.status).json({
        message: result.message
      });
    })
    .catch(function(err){
      console.log('FeederUnregisterERROR:'+err);
      res.status(err.status).json({
        message: err.message
      });
    });
});

// unregister Toilet
router.put('/unregister/toilet/:dogId', function(req, res){
  functionDog.setDeviceId(req.params.dogId, 22)
    .then(function(result){
      console.log('device:'+result);
      res.status(result.status).json({
        message: result.message
      });
    })
    .catch(function(err){
      console.log('ToiletUnregisterERROR:'+err);
      res.status(err.status).json({
        message: err.message
      });
    });
});

// Remove Feed Schedule 
router.post('/schedule/delete', function(req, res){
  var feederId = req.body.feederId;
  functionSchedule.removeScheduleByFeederId(feederId)
    .then(function(result){
      res.status(result.status).json({
        message: result.message
      });
    })
    .catch(function(err){
      res.status(err.status).json({
        message: err.message
      });
    });
})

// Feed Manually
router.post('/feed', function(req, res){
  var feederId = req.body.feederId;
  var feedTime = req.body.feedTime;
  var feedAmount = req.body.feedAmount;
  if (!feederId || !feedTime || !feedAmount){
    res.status(400).json({
      message: 'Invalid Request!'
    });
  }else{
    console.log(feederId);
    console.log(feedTime);
    console.log(feedAmount);
    res.status(200).json({
      message: 'Good!'
    });
  }

})

// Register Auto Feed Schedule
router.post('/schedule', function(req, res){
  var feederId = req.body.feederId;
  var feedTime = req.body.feedTime;
  var feedAmount = req.body.feedAmount;
  
  if (!feederId || !feedTime || !feedAmount){
    res.status(400).json({
      message: 'Invalid Request!'
    });
  }else{
    functionSchedule.registerSchedule(feederId, feedTime, feedAmount)
      .then(function(result){
        var jsonDataSchedule = {
          'feederId' : feederId,
          'feedTime' : feedTime,
          'feedAmount' : feedAmount
        }
        request.post({
            headers: {'content-type': 'application/json'},
            url: 'http://220.71.91.185:1111',
            body : jsonDataSchedule,
            json : true
          }, function(error, response, body){
            console.log('body:', body);
            //console.log('response:', response);
            // console.log('error', error)
        });
        res.status(result.status).json({
          message: result.message
        });
      })
      .catch(function(err){
        res.status(err.status).json({
          message: err.message
        });
      });
  }
})

// get Auto Feed Schedules
router.get('/schedule/:feederId', function(req, res){
  functionSchedule.getScheduleByFeederId(req.params.feederId)
    .then(function(result){
      res.json(result);
    })
    .catch(function(err){
      res.status(err.status).json({
        message : err.message
      });
    });
});






module.exports = router;