using Avalonia.Controls;
using System;
using Avalonia.Interactivity;

namespace Reservio;

public partial class NeueReservierungen : UserControl
{
    private string _kellnerName; 
    public NeueReservierungen(string kellnerName)
    {
        InitializeComponent();
        _kellnerName = kellnerName; 
        KellnerNameTextBlock.Text = $"Kellner: {kellnerName}";

        for (int i = 0; i < 24; i++)
        {
            hoursComboBox.Items
                .Add(i.ToString("D2")); // "D2" sorgt dafür, dass die Stunde immer zweiziffrig ist (z.B. "01")
        }

// Minuten von 00 bis 55 in 5-Minuten-Schritten hinzufügen
        for (int i = 0; i < 60; i += 1)
        {
            minutesComboBox.Items.Add(i.ToString("D2")); // "D2" sorgt für das Format "00", "05", "10", usw.
        }

        DateTime currentTime = DateTime.Now;
        hoursComboBox.SelectedItem = currentTime.ToString("HH"); // Aktuelle Stunde (24-Stunden-Format)
        minutesComboBox.SelectedItem = currentTime.ToString("mm"); // Aktuelle Minuten
    }

    private void Terrasse_Click(object? sender, RoutedEventArgs e)
    {
        this.Content = new Terrasse();
    }

    private void Freibereich_Click(object? sender, RoutedEventArgs e)
    {
        this.Content = new Freibereich();
    }

    private void Lounge_Click(object? sender, RoutedEventArgs e)
    {
        this.Content = new Lounge();
    }

    private void Gang_Click(object? sender, RoutedEventArgs e)
    {
        this.Content = new Gang(_kellnerName);
    }

    private void Saal_Click(object? sender, RoutedEventArgs e)
    {
        this.Content = new Saal();
    }
}

