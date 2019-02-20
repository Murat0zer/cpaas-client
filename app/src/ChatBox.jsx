import React from "react";
import SockJsClient from "react-stomp";
import UsernameGenerator from "username-generator";
import {TalkBox} from "react-talk";
import {connect} from "react-redux";

const randomString = require("random-string");

class ChatBox extends React.Component {
    constructor(props) {
        super(props);
        // randomUserId is used to emulate a unique user id for this demo usage
        this.randomUserName = UsernameGenerator.generateUsername("-");
        this.randomUserId = randomString({length: 20})
        this.state = {
            clientConnected: false,
            messages: []
        };

    }

    onMessageReceive = (msg, topic) => {

        console.log(topic);
        if(msg.chatMessageNotification) {
            let text = msg.chatMessageNotification.chatMessage.text;

            let message = {
                "author": msg.chatMessageNotification.chatMessage.senderAddress.split("@")[0],
                "authorId": msg.chatMessageNotification.chatMessage.senderAddress.split("@")[0],
                "message": text,
                "timestamp": Date.now().toString(),
                "status": msg.chatMessageNotification.chatMessage.status
            };

            console.log(text);
            this.setState(prevState => ({
                messages: [...prevState.messages, message]
            }));
        }

    };

    sendMessage = (msg, selfMsg) => {
        let chatMessage = {
            "chatMessage": {
                "text": selfMsg.message.toString()
            }
        };

        const {user, selectedUser} = this.props;
        // const receiver = user.username === 'user1' ? 'user2' : 'user1';
        const receiver = selectedUser;
        try {
            this.clientRef.sendMessage(`/user/${user.username}/chat/${receiver}`, JSON.stringify(chatMessage));
            this.setState(prevState => ({
                messages: [...prevState.messages, selfMsg]
            }));
            console.log(selfMsg)
            return true;
        } catch (e) {
            return false;
        }



    };
    // componentWillMount() {
    //   Fetch("/history", {
    //     method: "GET"
    //   }).then((response) => {
    //     this.setState({ messages: response.body });
    //   });
    // }

    render() {

        const {user} = this.props;

        return (
            <div>
                <TalkBox topic="Chat with "
                         currentUserId={this.randomUserId}
                         currentUser={user.firstName}
                         messages={this.state.messages}
                         onSendMessage={this.sendMessage}
                         connected={this.state.clientConnected}
                />

                <SockJsClient
                    headers={{'x-auth-token': user.token}}
                    url='http://localhost:8080/websocket'
                    topics={[`/user/notifications`]}
                    onMessage={this.onMessageReceive}
                    ref={(client) => {
                        this.clientRef = client
                    }}

                    onConnect={() => {
                        this.setState({clientConnected: true})
                    }}
                    onDisconnect={() => {
                        this.setState({clientConnected: false})
                    }}
                    debug={false}/>
            </div>
        );
    }
}
//////////////
function mapStateToProps(state) {
    const {authentication, contacts, chat } = state;
    const {user} = authentication;
    return {
        user,
        contacts,
        chat
    };
}

const connectedChatBox = connect(mapStateToProps)(ChatBox);
export {connectedChatBox as ChatBox};
