import { Link } from "react-router-dom";

export default function ErrorPage() {
  return (
    <div className="h-screen bg-gray-100 flex flex-col items-center gap-y-4">
      <p className="text-gray-600 text-4xl mt-24">Oops! page not found :(</p>
      <Link className="text-gray-600 text-xl underline" to="/">
        Go back
      </Link>
    </div>
  );
}
