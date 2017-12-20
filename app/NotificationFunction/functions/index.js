'user strict'
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
exports.sendNotifications = functions.database.ref('/Notifications/{user_id}/{notification_id}').onWrite(event => {
	const user_id = event.params.user_id;
	const notification_id = event.params.notification_id;

	console.log('we have a notification to send to :',user_id);
	if(!event.data.val()){
		return console.log('A Notification has been deleted from db',notification_id);
	}

	const fromUser = admin.database().ref(`/Notifications/${user_id}/${notification_id}`).once('value');
	return fromUser.then(fromUserResult => {
		const from_user_id = fromUserResult.val().from;
		console.log('You have new Notification from',from_user_id);
		const userQuery = admin.database().ref(`Users/${from_user_id}/name`).once('value');
		return userQuery.then(userResult => {
			const userName = userResult.val();
					const deviceToken = admin.database().ref(`/Users/${user_id}/device_token`).once('value');

	return deviceToken.then(result=>{

		const token_id = result.val();
			const payload ={
		notification: {
			title : "Friend Request",
			body : `${userName} has sent you a friend request `,
			icon : "bada",
			click_action : "com.example.bada.chatandplay_Target_Notification"
		},
		data :{
			userID : from_user_id
		}
	};

	return admin.messaging().sendToDevice(token_id,payload).then(response =>{
			console.log('This was the notification feature');

	});	

	});


});

	});
		});


	
