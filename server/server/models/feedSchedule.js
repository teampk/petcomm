var mongoose = require('mongoose');

var feedScheduleSchema = mongoose.Schema({
    feederId     : String,
    feedOrder : Number,
	feedTime   : String,
    feedAmount   : String,
    created_at : String
});

module.exports = mongoose.model('feedSchedule', feedScheduleSchema);
