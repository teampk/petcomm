var mongoose = require('mongoose');
var healthEatSchema = mongoose.Schema({

    feederId : String,
    feedAmount : String,
    created_at  : String
});

module.exports = mongoose.model('health_eat', healthEatSchema);
