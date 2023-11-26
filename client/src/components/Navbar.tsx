import { useDispatch, useSelector } from "react-redux";
import { KanriBrand } from "./KanriBrand";
import { authSelector, signout } from "@/features/auth/store";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/shadcnui/ui/dropdown-menu";
import { FirebaseSignOutUser } from "@/firebase";
import { apiSlice } from "@/store/apiSlice";

function UserDropdown({ name, email }: { name: string; email: string }) {
  const dispatch = useDispatch();
  function handleSignout() {
    FirebaseSignOutUser();
    dispatch(signout());
    dispatch(apiSlice.util.resetApiState());
  }

  return (
    <DropdownMenu>
      <DropdownMenuTrigger>Hi, {name}</DropdownMenuTrigger>
      <DropdownMenuContent>
        <DropdownMenuItem>My Issues</DropdownMenuItem>
        <DropdownMenuItem>{email}</DropdownMenuItem>
        <DropdownMenuItem onClick={handleSignout}>Logout</DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}

export function Navbar() {
  const auth = useSelector(authSelector);
  return (
    <div className="w-full p-2 flex flex-row justify-center items-center bg-white">
      <div className="flex flex-row justify-between items-center w-4/5">
        <KanriBrand />
        {auth.isAuthenticated ? (
          <UserDropdown name={auth.name} email={auth.email} />
        ) : null}
      </div>
    </div>
  );
}
