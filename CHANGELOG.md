# Changelog

## [2.0.0] - 2026-01-17

### New features and improvements

- Refactor some code
- Added 3 new color model:
    - `CorelSquareColorPickerModel`
    - `CorelTriangleColorPickerModel`
    - `CorelRhombusColorPickerModel`
- Added 2 new option:
    - `colorAlphaEnabled` by default `true`
    - `colorPreviewEnabled` by default `true`

### Changed

- Removed migLayout and replaced it with a custom layout implementation to reduce external dependencies.

## [1.1.0] - 2025-12-27

### New features and improvements

- Add new ColorPipettePicker for `Windows` to picker color from screen (PR #4)
- Available two model (PR #3)
    - `DinoColorPickerModel.java` (default)
    - `DiskColorPickerModel.java` (new model)

### Changed

- Refactor some code
- Color preview now paint transparent background
- Changed ColorPickerSelectionModel to ColorPickerModel
- FlatLaf update to v3.7

## [1.0.1] - 2025-11-30

### Fixed bugs

- Fixed High DPI scaling on:
    - draw image hue
    - draw image color alpha
    - draw image color-component

## [1.0.0] - 2025-08-15

- Initial release