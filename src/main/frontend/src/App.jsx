import React,{ useState,useEffect,useCallback } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import axios from 'axios'
import {useDropzone} from 'react-dropzone'


const UserProfile = () => {
  const [userProfiles, setUserProfiles] = useState([]);
  const fetchUserProfiles = () => {
    axios.get('http://localhost:8080/api/v1/user-profile').then(res => {
      setUserProfiles(res.data);
    });
  };
  useEffect(() => {
    fetchUserProfiles();
  }, []);

  return userProfiles.map((userProfile, index) => (
    <div key={index}>
      {userProfile.userProfileId ? (
        <img
          src={`http://localhost:8080/api/v1/user-profile/${userProfile.userProfileId}/image/download?${Date.now()}`}
          alt="Profile"
          width="170"
          height="170"
          style={{ borderRadius: '50%' }}
        />
      ) : (
        <p>No profile image</p>
      )}
      <br />
      <br />
      <h1>{userProfile.userName}</h1>
      <Dropzone userProfileId={userProfile.userProfileId} fetchUserProfiles={fetchUserProfiles} />
      <br />
    </div>
  ));
};

function Dropzone({ userProfileId, fetchUserProfiles }) {
  const onDrop = useCallback(
    acceptedFiles => {
      const file = acceptedFiles[0];
      const formData = new FormData();
      formData.append('file', file);
      axios
        .post(`http://localhost:8080/api/v1/user-profile/${userProfileId}/image/upload`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        })
        .then(() => {
          fetchUserProfiles(); // Refresh after upload
        })
        .catch(error => {
          console.error('Error uploading file:', error);
        });
    },
    [userProfileId, fetchUserProfiles]
  );
  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });

  return (
    <div {...getRootProps()}>
      <input {...getInputProps()} />
      {isDragActive ? (
        <p>Drop the image here ...</p>
      ) : (
        <p>Drag 'n' drop profile image, or click to select profile image</p>
      )}
    </div>
  );
}

function App() {
  //const [count, setCount] = useState(0)

  return (
  <div className="App">
    <UserProfile />

  </div>
  );
}

export default App;
