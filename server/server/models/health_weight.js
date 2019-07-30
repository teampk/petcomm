var mongoose = require('mongoose');
var healthWeightSchema = mongoose.Schema({

    toiletId : String,
    weight : String,
    created_at  : String
});

module.exports = mongoose.model('health_weight', healthWeightSchema);
