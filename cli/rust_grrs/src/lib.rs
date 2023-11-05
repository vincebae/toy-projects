use anyhow::Result;
use std::io::Write;

pub fn find_matches(content: &str, pattern: &str, mut writer: impl Write) -> Result<()> {
    for line in content.lines() {
        if line.contains(pattern) {
            writeln!(writer, "{}", line)?;
        }
    }
    Ok(())
}

#[cfg(test)]
mod tests {
    #[test]
    fn find_a_match() {
        let mut found = Vec::new();
        let result = crate::find_matches("lorem ipsum\ndolor sit amet", "lorem", &mut found);
        assert!(result.is_ok());
        assert_eq!(found, b"lorem ipsum\n");
    }
}
