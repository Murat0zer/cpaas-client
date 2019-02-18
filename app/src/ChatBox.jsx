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
            messages: ["Test"]
        };

    }

    onMessageReceive = (msg, topic) => {
        console.log(msg.chatMessage.text);
        this.setState(prevState => ({
            messages: [...prevState.messages, msg.chatMessage.text]
        }));

    };

    sendMessage = (msg, selfMsg) => {
        let chatMessage = {
            "chatMessage": {
                "text": selfMsg.message.toString()
            }
        };

        const {user} = this.props;
        const receiver = user.username === 'user1' ? 'user2' : 'user1';
        try {
            this.clientRef.sendMessage(`/user/${user.username}/chat/${receiver}`, JSON.stringify(chatMessage), {'preferredUsername': user.nvsUser.preferred_username});
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
                <TalkBox topic="react-websocket-template"
                         currentUserId={this.randomUserId}
                         currentUser={user.firstName}
                         messages={this.state.messages}
                         onSendMessage={this.sendMessage}
                         connected={this.state.clientConnected}
                />

                <SockJsClient
                    headers={{'user': user.nvsUser.preferredUsername}}
                    url='http://localhost:8080/websocket'
                    topics={[`/notifications/${user.username}`]}
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
                    debug={true}/>
            </div>
        );
    }
}

function mapStateToProps(state) {
    const {authentication} = state;
    const {user} = authentication;
    return {
        user
    };
}

const connectedChatBox = connect(mapStateToProps)(ChatBox);
export {connectedChatBox as ChatBox};
