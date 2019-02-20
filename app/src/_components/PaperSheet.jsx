import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import {connect} from "react-redux";

const styles = theme => ({
    root: {
        ...theme.mixins.gutters(),
        paddingTop: theme.spacing.unit * 2,
        paddingBottom: theme.spacing.unit * 2,
        backgroundColor: '#3f51b5',
        border:5,
        borderColor: 'blue'
    },
});

function PaperSheet(props) {
    const { classes } = props;

    return (
        <div>
            <Paper className={classes.root} elevation={0}>
                <Typography component="p" style={{'color':'white'}}>
                    Contacts
                </Typography>
            </Paper>
        </div>
    );
}

PaperSheet.propTypes = {
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
const connectedPaperSheet = (connect(mapStateToProps)(withStyles(styles)(PaperSheet)));
export {connectedPaperSheet as PaperSheet};
