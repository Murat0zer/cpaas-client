import React from 'react';
import {Route, Router} from 'react-router-dom';
import {connect} from 'react-redux';

import {history} from '../_helpers/history';
import {alertActions} from '../_actions/alert.actions';
import {PrivateRoute} from '../_components/PrivateRoute';
import {HomePage} from '../HomePage/HomePage';
import {LoginPage} from '../LoginPage/LoginPage';
import Container from "reactstrap/es/Container";

class App extends React.Component {
    constructor(props) {
        super(props);

        const {dispatch} = this.props;
        history.listen((location, action) => {
            // clear alert on location change
            dispatch(alertActions.clear());
        });
    }

    render() {
        const {alert} = this.props;
        return (

                <Container className="jumbotron">
                    <Container className="container" align="center">
                        <Container>
                            {alert.message &&
                            <Container className={`alert ${alert.type}`}>{alert.message}</Container>
                            }
                            <Router history={history}>
                                <Container>
                                    <Container>
                                        <PrivateRoute exact path="/" component={HomePage}/>
                                    </Container>

                                    <Container>
                                        <Route path="/login" component={LoginPage}/>
                                    </Container>
                                </Container>
                            </Router>
                        </Container>
                    </Container>
                </Container>

        );
    }
}

function mapStateToProps(state) {
    const {alert} = state;
    return {
        alert
    };
}

const connectedApp = connect(mapStateToProps)(App);
export {connectedApp as App};