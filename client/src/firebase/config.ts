import * as z from "zod";

const config = {
  apiKey: import.meta.env.VITE_FIREBASE_API_KEY,
  authDomain: import.meta.env.VITE_FIREBASE_AUTH_DOMAIN,
  projectId: import.meta.env.VITE_FIREBASE_PROJECT_ID,
  appId: import.meta.env.VITE_FIREBASE_APP_ID,
};

const firebaseConfigSchema = z.object({
  apiKey: z.string().nonempty(),
  authDomain: z.string().nonempty(),
  projectId: z.string().nonempty(),
  appId: z.string().nonempty(),
});

export function getFirebaseConfig() {
  try {
    return firebaseConfigSchema.parse(config);
  } catch (err) {
    console.log("Invalid Firebase config provided");
    if (err instanceof z.ZodError) {
      console.log(err.issues);
    }
    throw err;
  }
}
