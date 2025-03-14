/*
 * Copyright (C) 2023 Dynamic Solutions
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

'use strict';

const path = require('path');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const TSConfigPathsPlugin = require('tsconfig-paths-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const FileManagerPlugin = require('filemanager-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const ESLintPlugin = require('eslint-webpack-plugin');
const { WebpackManifestPlugin } = require('webpack-manifest-plugin');

const SOURCE_ROOT = __dirname + '/src';

const resolve = {
  extensions: ['.js', '.ts'],
  plugins: [
    new TSConfigPathsPlugin({
      configFile: './tsconfig.json',
    }),
  ],
};

module.exports = {
  resolve: resolve,
  entry: {
    main: { import: `${SOURCE_ROOT}/main.ts`, filename: 'main/main.[contenthash].js' },
    'main.published': {
      import: `${SOURCE_ROOT}/main.published.ts`,
      filename: 'main/main.published.[contenthash].js',
    },
    author: {import: SOURCE_ROOT + '/author.ts', filename: 'author/author.js'}
  },
  output: {
    filename: (chunkData) => {
      return chunkData.chunk.name === 'dependencies'
          ? 'dependencies/[name].js'
          : '[name].js';
    },
    path: path.resolve(__dirname, 'dist'),
  },
  module: {
    rules: [
      {
        test: /\.tsx?$/,
        exclude: /node_modules/,
        use: [
          {
            loader: 'ts-loader',
          },
          {
            loader: 'glob-import-loader',
            options: {
              resolve: resolve,
            },
          },
        ],
      },
      {
        test: /\.scss$/,
        use: [
          MiniCssExtractPlugin.loader,
          {
            loader: 'css-loader',
            options: {
              url: false,
            },
          },
          {
            loader: 'postcss-loader',
            options: {
              plugins() {
                return [require('autoprefixer')];
              },
            },
          },
          {
            loader: 'sass-loader',
          },
          {
            loader: 'glob-import-loader',
            options: {
              resolve: resolve,
            },
          },
        ],
      },
      {
        test: /\.css$/,
        use: [
          MiniCssExtractPlugin.loader,
          {
            loader: 'css-loader',
            options: {
              url: false
            }
          },
          {
            loader: 'postcss-loader',
            options: {
              plugins() {
                return [
                  require('autoprefixer')
                ];
              }
            }
          },
        ]
      },
    ],
  },
  plugins: [
    new CleanWebpackPlugin(),
    new ESLintPlugin(),
    new MiniCssExtractPlugin({
      filename: (file) => {
        return file.chunk.name === 'author' ? 'author/[name].css': 'main/[name].[contenthash].css'
      }
    }),
    new CopyWebpackPlugin({
      patterns: [
        {
          from: path.resolve(__dirname, SOURCE_ROOT + '/resources'),
          to: './main',
        },
        {
          from: path.resolve(
              __dirname,
              'webfragment/webfragment.editor.main.js'
          ),
          to: './main',
        },
        { from: path.resolve(__dirname, 'libs'), to: './main' },
      ],
    }),
    new WebpackManifestPlugin({
      fileName: 'main/versioned-resources-manifest.json',
      publicPath: '/libs/kyanite/webroot',
      filter: (file) => {
        return !file.isAsset && file.name.indexOf('author') < 0;
      },
      map: (file) => {
        file.path = file.path.replace('/main', '');
        return file;
      },
    }),
    new FileManagerPlugin({
      events: {
        onEnd: {
          copy: [
            {
              source: path.join(__dirname, 'dist/main'),
              destination: path.join(
                  __dirname,
                  'src/main/resources/libs/kyanite/webroot'
              ),
            },
            {
              source: path.join(__dirname, 'dist/author'),
              destination: path.join(
                  __dirname,
                  'src/main/resources/libs/kyanite/author'
              )
            }
          ],
        },
      },
    }),
  ],
  stats: {
    assetsSort: 'chunks',
    builtAt: true,
    children: false,
    chunkGroups: true,
    chunkOrigins: true,
    colors: false,
    errors: true,
    errorDetails: true,
    env: true,
    modules: false,
    performance: true,
    providedExports: false,
    source: false,
    warnings: true,
  },
};
