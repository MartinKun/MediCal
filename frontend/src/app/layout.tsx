import localFont from "next/font/local";
import "./globals.css";
import Header from "./common/Header";
import { Loader } from "@/components";
import { metadata } from "./config";

const geistSans = localFont({
  src: "./fonts/GeistVF.woff",
  variable: "--font-geist-sans",
  weight: "100 900",
});
const geistMono = localFont({
  src: "./fonts/GeistMonoVF.woff",
  variable: "--font-geist-mono",
  weight: "100 900",
});

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <head>
        <meta name="description" content={metadata.description} />
        <title>{metadata.title}</title>
      </head>
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased`}
      >
        <div
          className="min-h-screen
                     bg-gradient-to-br
                     from-purple-400
                     via-pink-500
                     to-red-500"
        >
          <Loader />
          <Header />
          {children}
        </div>
      </body>
    </html>
  );
}
