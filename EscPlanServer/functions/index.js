const functions = require('firebase-functions');
const TOP_USR_CORR = 11;

exports.ranker = functions.database.ref('/ranks/{uid}').onWrite(event => {
	const newUid = event.params.uid;
	const newRank = event.data.val();
	const newRankRooms = Object.keys(newRank);
	const root = event.data.ref.root;
	const recommends = root.child(`${uid}/recommended`);
	var correlation = {};
	var scores = {};
	return root.child('/ranks').once('value').then(snap => {
		snap.forEach(function(child) {
			if (child.key != uid) {
				let otherRank = child.val();
				correlation[child.key] = {
					'rank' : corr(otherRank, newRank),
					'rooms' : Object.keys(otherRank)
				};
			}
		});
		var topCorrelated = Object.keys(correlation).sort(function(a, b) {
			return correlation[a]['rank'] - correlation[b]['rank'];
		});
		for (i = 0; i < Math.min(TOP_USR_CORR, topCorrelated.length); i++) {
			getDeltaRooms(newRankRooms, correlation[topCorrelated[i]]['rooms'])
				.forEach(function(room) {
					if (!scores[room]) {
						scores[room] = 0;
					}
					scores[room] += correlation[topCorrelated[i]]['rank'];
				});
		}

		var maxVal = Math.max.apply(null, Object.keys(scores).map(key => scores[key])) / 100;
		for(var key in scores) {
			if(scores.hasOwnProperty(key)) {
				scores[key] /= maxVal;
			}
		}
		return recommends.set(scores);
	});


});

exports.rankAll = functions.https.onRequest((request, response) => {
	var root = functions.database.ref();
	var promises = [];

	root.child('/ranks').once('value').then(snap => {
		snap.forEach(function(child) {
			scores = [];
			var childRank = child.val();
			var childRankRooms = Object.keys(childRank);

			snap.forEach(function(brother) {
				if (child.key != brother.key) {
					var brotherRank = brother.val();
					correlation[child.key] = {
						'rank' : corr(childRank, brotherRank),
						'rooms' : Object.keys(brotherRank)
					};
				}
			});
			var topCorrelated = Object.keys(correlation).sort(function(a, b) {
				return correlation[a]['rank'] - correlation[b]['rank'];
			});
			for (i = 0; i < Math.min(TOP_USR_CORR, topCorrelated.length); i++) {
				getDeltaRooms(childRankRooms, correlation[topCorrelated[i]]['rooms'])
					.forEach(function(room) {
						if (!scores[room]) {
							scores[room] = 0;
						}
						scores[room] += correlation[topCorrelated[i]]['rank'];
					});
			}

			var maxVal = Math.max.apply(null, Object.keys(scores).map(key => scores[key])) / 100;
			for(var key in scores) {
				if(scores.hasOwnProperty(key)) {
					scores[key] /= maxVal;
				}
			}
		});
		promises.push(root.child(`${child.key}/recommended`).set(scores));
	});


	return Promise.all(promises).then(results => {
		response.send("Ranked sucessfully!");
	});
});

function getDeltaRooms(base, other) {
	var diff = [];
	other.forEach(function(room) {
		if (base.indexOf(room) < 0) {
			diff.push(room);
		}
	});
	return diff;
}

function corr(x, y) {
	var product = 0;
	Object.keys(x).forEach(function(room) {
		if (y[room]) {
			product += x[room] * y[room];
		}
	});
	product /= (norm(x)*norm(y));
	return product;
}

function norm(x) {
	var norm = 0;
	Object.keys(x).forEach(function(room) {
		norm += x[room]**2
	});
	return Math.sqrt(norm);
}
