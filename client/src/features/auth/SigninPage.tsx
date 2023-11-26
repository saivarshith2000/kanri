import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";

import { Button } from "@/shadcnui/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/shadcnui/ui/form";
import { Input } from "@/shadcnui/ui/input";
import { Link, useNavigate } from "react-router-dom";
import { AuthHeader } from "./components/AuthHeader";
import { firebaseSignin } from "@/firebase";
import { useDispatch } from "react-redux";
import { signin } from "./store";
import { toast } from "react-toastify";

const formSchema = z.object({
  email: z.string().email(),
  password: z.string().nonempty(),
});

function SigninForm() {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  const dispatch = useDispatch();
  const navigate = useNavigate();

  async function onSubmit(values: z.infer<typeof formSchema>) {
    const result = await firebaseSignin(values.email, values.password);
    form.reset();
    if (result.user) {
      toast.success("Sign In Successful!");
      dispatch(signin(result.user));
      navigate("/projects/");
    } else {
      toast.error(result.error);
    }
  }

  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className="space-y-4 w-[350px]"
      >
        <FormField
          control={form.control}
          name="email"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Email</FormLabel>
              <FormControl>
                <Input placeholder="Your e-mail address" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="password"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Password</FormLabel>
              <FormControl>
                <Input placeholder="Your password" {...field} type="password" />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <Button
          type="submit"
          className="w-full bg-green-600 hover:bg-green-700"
        >
          Sign In
        </Button>
      </form>
    </Form>
  );
}

export function SigninPage() {
  return (
    <div className="min-h-screen flex flex-col justify-content items-center">
      <AuthHeader />
      <div className="flex flex-col gap-y-4 mt-36">
        <p className="text-4xl">Sign In</p>
        <SigninForm />
        <Link to="/auth/signup" className="text-sm text-center text-blue-500">
          Don't have an account ? Create one now!
        </Link>
      </div>
    </div>
  );
}
