var mongoose = require('mongoose');

var feedScheduleSchema = mongoose.Schema({
    feederId     : String,
	feedTime   : String,
    feedAmount   : String,
    created_at : String
});

module.exports = mongoose.model('feedSchedule', feedScheduleSchema);
