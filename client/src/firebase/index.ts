import { FirebaseError, initializeApp } from "firebase/app";
import {
  getAuth,
  signOut,
  signInWithEmailAndPassword,
  createUserWithEmailAndPassword,
} from "firebase/auth";
import { getFirebaseConfig } from "./config";

const app = initializeApp(getFirebaseConfig());
const auth = getAuth(app);

export type FirebaseSigninResponse = {
  user?: { name: string; email: string };
  error?: string;
};

export async function firebaseSignin(
  email: string,
  password: string
): Promise<FirebaseSigninResponse> {
  try {
    const response = await signInWithEmailAndPassword(auth, email, password);
    return {
      user: {
        email: response.user.email!,
        name: response.user.displayName
          ? response.user.displayName
          : "Kanri User",
      },
    };
  } catch (err) {
    if (err instanceof FirebaseError) {
      if (err.code === "auth/invalid-login-credentials") {
        return { error: "Invalid Credentials" };
      } else if (err.code === "auth/network-request-failed") {
        return { error: "Network unavailable" };
      }
    }
    return { error: "An unexpected error occured, please try again later" };
  }
}

export async function getToken() {
  const currentUser = getAuth().currentUser;
  if (currentUser) {
    return await currentUser.getIdToken();
  } else {
    throw new Error("User not signed in");
  }
}

export const signUpUser = async (email: string, password: string) => {
  if (!email && !password) return;

  return await createUserWithEmailAndPassword(auth, email, password);
};

export const FirebaseSignOutUser = async () => await signOut(auth);
