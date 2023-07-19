# Atomic design

We use Atomic design.

### Showcase (TLDR)

https://bradfrost.com/blog/post/atomic-web-design/

### Original book

It's free, hosted on the following link. ChatGPT knows all its contents you can ask it for summary of each chapter.
https://atomicdesign.bradfrost.com/table-of-contents/

# A-BEM

We use BEM, if it's sensible you can also use Atomic BEM https://css-tricks.com/abem-useful-adaptation-bem/

# SCSS `@extend` vs `@include`

- `@extend` is reusable and produces less CSS size. https://sass-lang.com/documentation/at-rules/extend
- `@include` copy-pastes the CSS making it bigger. https://sass-lang.com/documentation/at-rules/mixin

Tradeoff: `@include` duplicates CSS rules, `@extend` creates many selectors.
Whenever possible check if CSS produced with `@extend` is smaller as we aim for low footprint.

You can use "placeholders" feature `%` of SCSS to minimize redundant selectors in build output, see:
https://sass-lang.com/documentation/style-rules/placeholder-selectors

### Note: `@extend` does not work with media queries

Bug since 2013 https://github.com/sass/sass/issues/1050

That's why mobile first uses `@extend`, but mobile breakpoints use `@include` as `@extend` would not work there
(SCSS would fail to build).

```
.a-title,
.a-subtitle {
@extend .t-letter-spacing--default;

    &-1 {
        @extend .t-font-size--56;
        @extend .t-line-height--100;

        @include breakpoint.media-breakpoint-medium {
            $min: map.get(font.$map_t-font-sizes, '56');
            $max: map.get(font.$map_t-font-sizes, '128');
            @include font.t-font-size-fluid($min, $max);
        }

        @include breakpoint.media-breakpoint-maximum {
            @include font.t-font-size(128);
```
