import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
import PersonIcon from '@material-ui/icons/Person';
import DraftsIcon from '@material-ui/icons/Drafts';
import {connect} from "react-redux";
import {userActions} from "../_actions/user.actions";

const styles = theme => ({
    root: {
        width: '100%',
        maxWidth: 360,
        backgroundColor: theme.palette.background.paper,
    },
});


class ContactList extends React.Component {
    state = {
        selectedIndex: 1,
    };


    componentDidMount() {
        const { user } = this.props;
        this.props.dispatch(userActions.getContacts(user.username));
    }
    handleListItemClick = (event, index) => {
        this.setState({ selectedIndex: index });
    };

    render() {
        const { classes } = this.props;

        return (
            <div className={classes.root}>
                <List component="nav">
                    <ListItem
                        button
                        selected={this.state.selectedIndex === 0}
                        onClick={event => this.handleListItemClick(event, 0)}
                    >
                        <ListItemIcon>
                            <PersonIcon />
                        </ListItemIcon>
                        <ListItemText primary="User 1" />
                    </ListItem>
                </List>
                <Divider />
                <List>
                    <ListItem
                        button
                        selected={this.state.selectedIndex === 1}
                        onClick={event => this.handleListItemClick(event, 1)}
                    >
                        <ListItemIcon>
                            <PersonIcon />
                        </ListItemIcon>
                        <ListItemText primary="User 2" />
                    </ListItem>
                </List>
                <Divider />
            </div>
        );
    }
}

ContactList.propTypes = {
    classes: PropTypes.object.isRequired,
};

function mapStateToProps(state) {
    const {authentication, contacts} = state;
    const {user} = authentication;
    return {
        user,
        contacts
    };
}

const connectedContactList = (connect(mapStateToProps)(withStyles(styles)(ContactList)));
export {connectedContactList as ContactList};
