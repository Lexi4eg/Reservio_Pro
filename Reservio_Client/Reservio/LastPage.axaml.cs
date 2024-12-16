using System;
using Avalonia.Controls;
using Avalonia.Interactivity;

namespace Reservio
{
    public partial class LastPage : UserControl
    {
        public LastPage()
        {
            InitializeComponent();
        }

        private void OnHauptseiteClick(object? sender, RoutedEventArgs e)
        {
            this.Content = new Datenerhebung();
        }
    }
}
