import React from 'react'
import SockJsClient from 'react-stomp';

class Message extends React.Component {
    constructor(props) {
        super(props);
        this.state = {value: ''};

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({value: event.target.value});
    }

    handleSubmit(event) {
        this.sendMessage("Hello")
        event.preventDefault();
    }

    sendMessage = (msg) => {
       this.clientRef.sendMessage('/chat', msg);
       console.log(this.clientRef.valueOf())
    }

    connect = () => {
        console.log("connect oldu")
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <label>
                    Message:
                    <input type="text" value={this.state.value} onChange={this.handleChange}/>
                </label>
                <input type="submit" value="Submit"/>

                <button onClick={this.connect()}/>
                <div>

                    <SockJsClient url='http://localhost:8080/websocket'
                                  topics={['/notifications']}
                                  onMessage={(msg) => {
                                      console.log(msg);
                                  }}
                                  onConnect={this.connect}
                                  ref={(client) => {
                                      this.clientRef = client
                                  }}/>

                </div>
            </form>
        );
    }
}

export default Message;