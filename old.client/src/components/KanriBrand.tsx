import { Link } from "react-router-dom";

export function KanriBrand() {
  return (
    <Link
      to={import.meta.env.VITE_KANRI_HOME}
      className="tracking-widest font-extrabold text-transparent text-4xl bg-clip-text bg-gradient-to-r from-pink-400 to-red-500"
    >
      KANRI
    </Link>
  );
}
