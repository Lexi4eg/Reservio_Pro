using Avalonia.Controls;
using System;
using Avalonia.Interactivity;

namespace Reservio;

public partial class Gang : UserControl
{
    private string _kellnerName;

    public Gang(string kellnerName)
    {
        InitializeComponent();
        _kellnerName = kellnerName;  // Speichere den Namen für später
    }

    private void Weiter_Click(object? sender, RoutedEventArgs e)
    {
        

        var parentWindow = this.VisualRoot as Window;
        if (parentWindow != null)
        {
            parentWindow.Content = new NeueReservierungen(_kellnerName);
        }
    }
}

