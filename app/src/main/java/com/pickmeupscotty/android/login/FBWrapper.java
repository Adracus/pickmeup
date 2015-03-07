package com.pickmeupscotty.android.login;

import android.os.Handler;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.facebook.SessionState;
import com.facebook.model.GraphUser;

/**
 * Created by florian on 07.03.15.
 */
public enum FBWrapper implements Session.StatusCallback {
    INSTANCE();

    private ArrayList<FacebookLoginStateListener> stateChangeListener;

    private List<GraphUser> myFriends;
    private GraphUser me;

    FBWrapper() {
        stateChangeListener = new ArrayList<>();
    }

    public void addFacebookLoginStateListener(FacebookLoginStateListener l) {
        stateChangeListener.add(l);
    }

    private void fireFacebookLoginStateListener(SessionState sessionState) {
        for (FacebookLoginStateListener l : stateChangeListener) {
            l.onStateChanged(sessionState);
        }
    }

    @Override
    public void call(Session session, SessionState sessionState, Exception e) {
        fireFacebookLoginStateListener(sessionState);
    }

    public void getFBFriends(final Request.GraphUserListCallback callback) {
        Session session = Session.getActiveSession();

        Objects.requireNonNull(session);

        if (myFriends != null) {
            callback.onCompleted(myFriends, null);
            return;
        }

        Request.newMyFriendsRequest(session, new Request.GraphUserListCallback() {
            @Override
            public void onCompleted(List<GraphUser> graphUsers, Response response) {
                myFriends = graphUsers;

                callback.onCompleted(graphUsers, response);
            }
        }).executeAsync();
    }

    public void isMyFriend(final String userID, final IsMyFriendCallback callback) {
        getFBFriends(new Request.GraphUserListCallback() {
            @Override
            public void onCompleted(List<GraphUser> graphUsers, Response response) {
                for (GraphUser u : graphUsers) {
                    if (u.getId().equals(userID)) {
                        callback.onCompleted(true);
                        return;
                    }
                }

                callback.onCompleted(false);
            }
        });
    }

    public void getMyUser(final Request.GraphUserCallback callback) {
        Session session = Session.getActiveSession();

        Objects.requireNonNull(session);

        if (me != null) {
            callback.onCompleted(me, null);
        }

        Request.newMeRequest(session, new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                me = user;

                callback.onCompleted(user, response);
            }
        }).executeAsync();
    }

    public void getUserId(final UserIdCallback callback) {
        getMyUser(new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser graphUser, Response response) {
                callback.onCompleted(me.getId());
            }
        });
    }

    public interface UserIdCallback {
        public void onCompleted(String fbid);
    }

    public interface IsMyFriendCallback {
        public void onCompleted(boolean isMyFriend);
    }

    public interface FacebookLoginStateListener {
        public void onStateChanged(SessionState sessionState);
    }
}
