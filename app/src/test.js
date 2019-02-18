import React from 'react';
import SockJsClient from 'react-stomp';

class Test extends React.Component {
    constructor(props) {
        super(props);
        this.state = {client : new SockJsClient}

    }

    sendMessage = (msg) => {
        this.clientRef.sendMessage('api/users/chat', msg);
    }

    render() {
        return (
            <div>
                <SockJsClient url='http://localhost:8080/websocket' topics={['/notifications']}
                              onMessage={(msg) => { console.log(msg); }}
                              ref={ (client) => { this.clientRef = client }} />
            </div>
        );
    }
}

export default Test;