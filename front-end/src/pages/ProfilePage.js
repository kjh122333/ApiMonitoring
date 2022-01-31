import React from 'react';
import ProfileContainer from '../containers/user/ProfileContainer';

const ProfilePage = () => {
  window.scrollTo(0,0);
  return (
    <div>
      <div className="table-operations">
        <p>프로필</p>
      </div>
      <hr />
      <ProfileContainer />
    </div>
  );
};

export default ProfilePage;
