var scheduledb = require('../models/feedSchedule');

exports.registerSchedule = (mFeederId, mFeedTime, mFeedAmount) =>
    new Promise(
        function(resolve, reject){
            var newSchedule = new scheduledb({
                feederId : mFeederId,
                feedTime : mFeedTime,
                feedAmount : mFeedAmount,
                created_at : new Date()
            });

            newSchedule.save()
            .then(function(){
                console.log('-- Schedule Registered --');
                resolve({ status: 201, message: 'Schedule Registered Sucessfully!'});
            })
            .catch(function(err){
                reject({status:500, message: 'Internal Server Error !'});
            });


        }
    );

exports.removeScheduleByFeederId = feederId =>
    new Promise(
        function(resolve, reject){
            /*
            var newSchedule = new scheduledb({
                feederId : feederId,
                feedTime : '03:00',
                feedAmount : '40',
                created_at : new Date()
            });
            newSchedule.save()
            */
        
            scheduledb.deleteMany({feederId : feederId})
            .then(function(){
                resolve({status:201, message: 'Remove Complete'});
            })
            .catch(function(err){
                if (err.code==10000){
                    reject({status:409, message:'already registered'});
                }else{
                    reject({status:500, message:'Internal Server Error'});
                }
            })
        }
    );

exports.getScheduleByFeederId = feederId =>
    new Promise(
        function(resolve, reject){
            scheduledb.find({feederId : feederId})
            .then(function(schedules){
                console.log('-- Getting Schedules... --');
                resolve(schedules);
            })
            .catch(function(err){
                reject({status, message: 'Internal Server Error :('});
            })
        }
    );