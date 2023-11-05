use anyhow::{Context, Result};
use clap::Parser;
use log::info;
use std::io;

#[derive(Parser)]
struct Cli {
    pattern: String,
    path: std::path::PathBuf,
}

#[derive(Debug)]
struct CustomError(String);

fn main() -> Result<()> {
    // In order to manually parse the arguments:
    // let pattern = std::env::args().nth(1).expect("no pattern given");
    // let path = std::env::args().nth(2).expect("no path given");
    // let args = Cli {
    //     pattern,
    //     path: std::path::PathBuf::from(path),
    // };
    //
    // Various ways to open a file and handle errors:
    // let content = std::fs::read_to_string(&args.path).expect("could not read file");
    // let content = std::fs::read_to_string(&args.path).unwrap();
    // let result = std::fs::read_to_string(&args.path);
    // let content = match result {
    //     Ok(content) => { content },
    //     Err(error) => { return Err(error.into()); }
    // };
    // let content = std::fs::read_to_string(&args.path)?;
    // let content = std::fs::read_to_string(&args.path)
    //     .map_err(|err| CustomError(format!("Error reading `{:?}`: {}", &args.path, err)))?;

    env_logger::init();
    info!("starting...");
    let args = Cli::parse();
    let content = std::fs::read_to_string(&args.path)
        .with_context(|| format!("could not read file {:?}", &args.path))?;
    let mut handle = io::BufWriter::new(io::stdout());
    rust_grrs::find_matches(&content, &args.pattern, &mut handle)
}
