var express = require('express');
var router = express.Router();
var functionHealth = require('../functions/functionHealth');


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
      functionHealth.registerHealthEat(feederId, feedAmount)
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
    }
  
  })




module.exports = router;