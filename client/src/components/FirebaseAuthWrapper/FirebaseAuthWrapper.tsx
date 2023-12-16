import { signin, signout } from '@/pages/signin/store';
import { FirebaseSignOutUser } from '@/firebase';
import { getAuth, onAuthStateChanged } from 'firebase/auth';
import { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import { Outlet } from 'react-router-dom';
import { Loader } from '@mantine/core';

export function FirebaseAuthWrapper() {
  const dispatch = useDispatch();
  const auth = getAuth();
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    // Check if firebase session exists and restore it
    onAuthStateChanged(auth, (currentUser) => {
      if (currentUser) {
        dispatch(
          signin({
            email: currentUser.email!,
            name: currentUser.displayName ? currentUser.displayName : 'Kanri User',
          })
        );
      } else {
        dispatch(signout());
        FirebaseSignOutUser();
      }
      setLoading(false);
    });
  }, []);

  if (loading) {
    return (
      <div className="min-h-screen m-auto flex justify-center items-center">
        <Loader size="xl" />
      </div>
    );
  }

  return <Outlet />;
}
