import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Fab from '@material-ui/core/Fab';
import {connect} from "react-redux";
import Button from '@material-ui/core/Button';
import Icon from '@material-ui/core/Icon';
import AddIcon from '@material-ui/icons/Add';

const styles = theme => ({
    fab: {
        margin: theme.spacing.unit,
    },
    extendedIcon: {
        marginRight: theme.spacing.unit,
    },
});

function AddContactButton (props) {
    const { classes } = props;
    return (
        <div>
            <Button style={{'width' : "100%"}}  variant="contained" color="primary" className={classes.button}>
                Add
                <AddIcon className={classes.rightIcon} />
            </Button>
        </div>
    );
}

AddContactButton.propTypes = {
    classes: PropTypes.object.isRequired,
};

function mapStateToProps(state) {
    const {authentication, contacts } = state;
    const {user} = authentication;
    return {
        user,
        contacts
    };
}

const connectedAddContactButton = (connect(mapStateToProps)(withStyles(styles)(AddContactButton)));
export {connectedAddContactButton as AddContactButton};
