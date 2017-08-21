const functions = require('firebase-functions');
const TOP_USR_CORR = 5;

exports.ranker = functions.database
	.ref('/ranks/{uid}')
	.onWrite(event => {
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
			}
			var topCorrelated = Object.keys(correlation).sort(function(a, b) {
				return correlation[a]['rank'] - correlation[b]['rank'];
			}
			for (i = 0; i < Math.min(TOP_USR_CORR, topCorrelated.length); i++) {
				getDeltaRooms(newRankRooms, correlation[topCorrelated[i]]['rooms'])
					.forEach(function(room) {
						if (!scores[room]) {
							scores[room] = 0;
						}
						scores[room] += correlation[topCorrelated[i]]['rank'];
					}
			}

			return recommends.set(scores);
		}


});

function getDeltaRooms(base, other) {
	var diff = [];
	other.forEach(function(room) {
		if (base.indexOf(room) == -1) {
			diff.push(room);
		}
	}
	return diff;
}

function corr(x, y) {
	var product = o
	Object.keys(x).forEach(function(room) {
		if (y[room]) {
			product += x[room] * y[room];
		}
	}
	product /= (norm(x)*norm(y));
	return product;
}

function norm(x) {
	var norm = 0;
	Object.keys(x).forEach(function(room) {
		norm += x[room]**2
	}
	return Math.sqrt(norm);
}
