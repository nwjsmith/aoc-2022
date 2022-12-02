{
  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixpkgs-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };
  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
        jdk = pkgs.temurin-bin-11;
        clojure = pkgs.clojure.override { inherit jdk; };
      in { devShell = pkgs.mkShell { buildInputs = with pkgs; [ clojure clojure-lsp ]; }; });
}
