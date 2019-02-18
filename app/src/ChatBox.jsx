import React from "react";
import ReactDom from "react-dom";
import SockJsClient from "react-stomp";
import UsernameGenerator from "username-generator";
import {TalkBox} from "react-talk";

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
        console.log(msg.chatMessage.text)
        this.setState(prevState => ({
            messages: [...prevState.messages, msg.chatMessage.text]
        }));

    }

    sendMessage = (msg, selfMsg) => {
        let chatMessage = {
            "chatMessage": {
                "text": selfMsg.message.toString()
            }
        };
        try {
            this.clientRef.sendMessage("/user/chat", JSON.stringify(chatMessage));
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
        const wsSourceUrl = "http://localhost:8080/websocket";
        return (
            <div>
                <TalkBox topic="react-websocket-template" currentUserId={this.randomUserId}
                         currentUser={this.randomUserName} messages={this.state.messages}
                             onSendMessage={this.sendMessage} connected={this.state.clientConnected}/>

                <SockJsClient url={wsSourceUrl} topics={["/user/notifications"]}
                              onMessage={this.onMessageReceive} ref={(client) => {
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

export default ChatBox
