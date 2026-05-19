/*
 * Copyright (C) 2026 Dynamic Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

const iconCache = new Map()

async function loadIcon(name: String) {
  if (iconCache.has(name)) {
    return iconCache.get(name).cloneNode(true);
  }

  const response = await fetch(
    `https://cdn.jsdelivr.net/npm/@mdi/svg/svg/${name}.svg`
  )

  if (!response.ok) {
    throw new Error(`Cannot load icon: ${name}`);
  }

  const svgText = await response.text();
  const wrapper = document.createElement('div');
  wrapper.innerHTML = svgText.trim();

  const svg = wrapper.firstElementChild;

  if (!svg) {
    return;
  }

  svg.classList.add('mdi-svg');
  iconCache.set(name, svg);

  return svg.cloneNode(true);
}

export async function upgradeMdiIcons(root = document) {
  const elements = root.querySelectorAll('.mdi') as NodeListOf<HTMLElement>;

  for (const el of elements) {
    const mdiClass = [...el.classList].find(cls => {
        // looking for the icon name, ignoring the size classes (e.g. mdi-36px)
        const match = cls.match(/mdi-(?!\d+px)([a-zA-Z0-9-]+)$/);

        return match?.[1] || false;
      }
    )

    if (!mdiClass) continue;

    const iconName = mdiClass.replace('mdi-', '');

    try {
      const svg = await loadIcon(iconName);

      for (const cls of el.classList) {
        if (cls !== 'mdi' && !cls.startsWith('mdi-')) {
          svg.classList.add(cls);
        }
      }

      svg.style.cssText = el.style.cssText;

      for (const attr of el.attributes) {
        if (
          attr.name !== 'class' &&
          attr.name !== 'style'
        ) {
          svg.setAttribute(attr.name, attr.value);
        }
      }

      el.replaceWith(svg);
    } catch (err) {
      console.error(err);
    }
  }
}